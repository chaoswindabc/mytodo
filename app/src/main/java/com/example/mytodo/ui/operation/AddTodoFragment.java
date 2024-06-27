package com.example.mytodo.ui.operation;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.mytodo.AlarmReceiver;
import com.example.mytodo.DatabaseHelper;
import com.example.mytodo.MainActivity;
import com.example.mytodo.R;
import com.example.mytodo.ToDoItem;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class AddTodoFragment extends DialogFragment {

    private EditText titleInput;
    private EditText dateInput;
    private EditText timeInput;
    private DatabaseHelper databaseHelper;
    private OnTodoItemAddedListener todoItemAddedListener;
    private Context mContext;

    public AddTodoFragment() {
        // 空构造函数
    }

    public AddTodoFragment(Context context, DatabaseHelper dbHelper) {
        // 构造函数，接收一个DatabaseHelper对象
        this.mContext = context;
        this.databaseHelper = dbHelper;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化DatabaseHelper
        databaseHelper = new DatabaseHelper(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_todo, container, false);

        titleInput = view.findViewById(R.id.title_input);
        dateInput = view.findViewById(R.id.date_input);
        Button addButton = view.findViewById(R.id.add_button);
//        Button backButton = view.findViewById(R.id.back_button);

        // 设置日期输入框的点击监听器
        dateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        timeInput = view.findViewById(R.id.time_input);

        // 设置时间输入框的点击监听器
        timeInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog();
            }
        });

        // 设置添加按钮的点击监听器
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleInput.getText().toString();
                String dateTime = dateInput.getText().toString() + " " + timeInput.getText().toString()+ ":00";
                boolean isCompleted = false;

                // 插入新的待办事项到数据库
                long newId = databaseHelper.insertTodoItem(title, dateTime, isCompleted);
                System.out.println(convertDateTimeStringToTimeStamp(dateTime));
                setAlarm(mContext,convertDateTimeStringToTimeStamp(dateTime),title, (int) newId);
                //                if (navController != null) {
//                    navController.popBackStack();
//                } else {
//                    // NavController 为 null，处理错误
//                    Log.e("AddTodoFragment", "NavController is null");
//                }
                System.out.println("addToDo");
                for (ToDoItem item : databaseHelper.getTodoItems()) {
                    System.out.println("ToDoItem: " + item.getTitle() + ", " + item.getTime() + ", " + item.isCompleted());
                }
                if (todoItemAddedListener != null) {
                    todoItemAddedListener.onTodoItemAdded();
                }
                dismiss();
            }
        });

        return view;
    }

    public static long convertDateTimeStringToTimeStamp(String dateTimeString) {
        // 创建 SimpleDateFormat 对象，指定日期时间格式
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // 尝试解析日期时间字符串
        Date date;
        try {
            date = dateFormat.parse(dateTimeString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        // 获取日期时间字符串对应的时间戳
        return date.getTime();
    }

    private void showDatePickerDialog() {
        // 获取当前日期
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // 创建DatePickerDialog实例
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        // 更新日期输入框的文本
                        dateInput.setText(year + "-" + String.format("%02d", (month + 1)) + "-" + String.format("%02d", day));
                    }
                }, year, month, day);

        // 显示DatePickerDialog
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        timeInput.setText(String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute));
                    }
                }, hour, minute, false);

        timePickerDialog.show();
    }

    public void setOnTodoItemAddedListener(OnTodoItemAddedListener listener) {
        this.todoItemAddedListener = listener;
    }

    public interface OnTodoItemAddedListener {
        void onTodoItemAdded();
    }

    public void setAlarm(Context context, long time, String title, int id) {
        Intent intent = new Intent(mContext, AlarmReceiver.class);
        intent.putExtra("id", id);
        intent.putExtra("title", title);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, (int) id, intent, PendingIntent.FLAG_UPDATE_CURRENT| PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent);
    }
}
