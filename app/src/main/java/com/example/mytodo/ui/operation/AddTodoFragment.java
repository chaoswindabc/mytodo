package com.example.mytodo.ui.operation;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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

import com.example.mytodo.DatabaseHelper;
import com.example.mytodo.R;

import java.util.Calendar;

public class AddTodoFragment extends DialogFragment {

    private EditText titleInput;
    private EditText dateInput;
    private EditText timeInput;
    private DatabaseHelper databaseHelper;
    private NavController navController;

    public AddTodoFragment() {
        // 空构造函数
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

        navController = NavHostFragment.findNavController(this);
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
                databaseHelper.insertTodoItem(title, dateTime, isCompleted);

//                if (navController != null) {
//                    navController.popBackStack();
//                } else {
//                    // NavController 为 null，处理错误
//                    Log.e("AddTodoFragment", "NavController is null");
//                }

                dismiss();
            }
        });

        // 设置返回按钮的点击监听器
//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // 处理返回逻辑，可能是关闭对话框或导航回上一个界面
//            }
//        });

        return view;
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
}
