package com.example.mytodo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// 数据库帮助类，用于管理SQLite数据库的创建、升级和操作
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "todo_list.db";
    private static final int DATABASE_VERSION = 1;

    // 创建数据库和表的SQL语句
    private static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS items (" +
                    "    id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "    title TEXT NOT NULL, " +
//                    "    content TEXT, " +
                    "    time DATETIME NOT NULL, " +
//                    "    importance TEXT, " +
                    "    is_completed BOOLEAN NOT NULL DEFAULT FALSE " +//这条还没做
                    ");";

    // 构造函数
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //创建表
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public long insertTodoItem(String title, String time, boolean isCompleted) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("time", time);
        values.put("is_completed", isCompleted);
        long newId = db.insert("items", null, values);
        db.close();
        return newId;
    }

    public void oldDeleteTodoItem(String title) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("items", "title = ?", new String[]{title});
        db.close();
    }
    public void deleteTodoItem(long itemId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("items", "id = ?", new String[] { String.valueOf(itemId) });
        db.close();
    }

    public List<ToDoItem> getTodoItems() {
        SQLiteDatabase db = getReadableDatabase();
        List<ToDoItem> todoItems = new ArrayList<>();
        Cursor cursor = db.query("items", null, null, null, null, null, "time ASC");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        while (cursor.moveToNext()) {
            ToDoItem item = new ToDoItem();
            item.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            item.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
            String timeString = cursor.getString(cursor.getColumnIndexOrThrow("time"));
            try {
                Date time = sdf.parse(timeString);
                item.setTime(time);
            } catch (ParseException e) {
                // 处理解析异常
                e.printStackTrace();
            }
            item.setCompleted(cursor.getInt(cursor.getColumnIndexOrThrow("is_completed")) == 1);
            todoItems.add(item);
        }
        cursor.close();
        db.close();
        return todoItems;
    }
}
