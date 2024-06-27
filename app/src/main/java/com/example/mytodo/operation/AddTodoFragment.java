package com.example.mytodo.operation;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;

import com.example.mytodo.database.DatabaseHelper;
import com.example.mytodo.R;
import com.example.mytodo.recycler.ToDoItem;

import java.util.Calendar;

public class AddTodoFragment extends DialogFragment {

    private EditText titleInput;
    private EditText dateInput;
    private EditText timeInput;
    private DatabaseHelper databaseHelper;
    private OnTodoItemAddedListener todoItemAddedListener;

    public AddTodoFragment() {
    }

    public AddTodoFragment(DatabaseHelper dbHelper) {
        this.databaseHelper = dbHelper;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_todo, container, false);

        titleInput = view.findViewById(R.id.title_input);
        dateInput = view.findViewById(R.id.date_input);
        Button addButton = view.findViewById(R.id.add_button);

        // 设置日期
        dateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        timeInput = view.findViewById(R.id.time_input);

        // 设置时间
        timeInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog();
            }
        });

        // 设置默认值为当前时间
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        dateInput.setText(year + "-" + String.format("%02d", (month + 1)) + "-" + String.format("%02d", day));

        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        timeInput.setText(String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute));

        // 添加
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleInput.getText().toString();
                String dateTime = dateInput.getText().toString() + " " + timeInput.getText().toString()+ ":00";
                boolean isCompleted = false;

                // 插入新的待办事项到数据库
                databaseHelper.insertTodoItem(title, dateTime, isCompleted);
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

    private void showDatePickerDialog() {
        // 获取当前日期
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        // 更新日期输入框的文本
                        dateInput.setText(year + "-" + String.format("%02d", (month + 1)) + "-" + String.format("%02d", day));
                    }
                }, year, month, day);

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
}
