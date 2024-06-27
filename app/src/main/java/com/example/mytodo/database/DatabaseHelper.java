package com.example.mytodo.database;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mytodo.notifications.NotificationReceiver;
import com.example.mytodo.recycler.ToDoItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "todo_list.db";
    private static final int DATABASE_VERSION = 1;
    private Context context;

    private static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS items (" +
                    "    id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "    title TEXT NOT NULL, " +
                    "    time DATETIME NOT NULL, " +
                    "    is_completed BOOLEAN NOT NULL DEFAULT FALSE " +
                    ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void insertTodoItem(String title, String time, boolean isCompleted) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("time", time);
        values.put("is_completed", isCompleted);
        long itemId = db.insert("items", null, values);
        db.close();

        scheduleNotification((int)itemId, title, time);
    }

    public void deleteTodoItem(long itemId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("items", "id = ?", new String[]{String.valueOf(itemId)});
        db.close();

        cancelNotificationAndAlarm(itemId);
    }

    // 删除过期事项
    public void deleteExpireTodoItems() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTimeString = sdf.format(new Date());

        SQLiteDatabase db = getWritableDatabase();
        db.delete("items", "time < ?", new String[]{currentTimeString});
        db.close();
    }



    // 查询全表
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
                e.printStackTrace();
            }
            item.setCompleted(cursor.getInt(cursor.getColumnIndexOrThrow("is_completed")) == 1);
            todoItems.add(item);
        }
        cursor.close();
        db.close();
        return todoItems;
    }

    // 实现通知的设置和删除
    private void scheduleNotification(int itemId, String title, String timeString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;
        try {
            date = sdf.parse(timeString);
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }

        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("todo_text", title);
        intent.putExtra("id", itemId);
//        Log.d("Notification", "Scheduled notification for id: " + itemId);
        System.out.println("addID:"+itemId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, (int) itemId, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, date.getTime(), pendingIntent);
            System.out.println("getTime():"+date.getTime());
            System.out.println("currentTimeMillis():"+System.currentTimeMillis());
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+1000, pendingIntent);
        }
    }

    private void cancelNotificationAndAlarm(long itemId) {
        // 取消通知
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.cancel((int) itemId);
        }

        // 取消定时器
        Intent intent = new Intent(context, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, (int) itemId, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }
}
