package com.example.mytodo.ui.recycler;

import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.service.notification.StatusBarNotification;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mytodo.*;

import java.util.List;

// 适配器类，用于绑定ToDoItem数据模型到RecyclerView的列表项
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    // 数据源，包含所有的ToDoItem对象
    private final List<ToDoItem> mValues;
    private Context mContext;
    NotificationManager notificationManager;

    // 构造函数，接收一个ToDoItem列表作为参数
    public RecyclerViewAdapter(Context context,List<ToDoItem> items) {
        mContext = context;
        mValues = items;
        notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    // 创建视图持有者（ViewHolder）的方法
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 使用LayoutInflater和传入的父视图（parent）来创建新的视图
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todo_item, parent, false);
        // 返回一个新的ViewHolder对象，它包含这个新视图的引用
        return new ViewHolder(view);
    }

    // 将数据绑定到视图持有者（ViewHolder）的方法
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // 从数据源中获取当前的ToDoItem对象
        ToDoItem toDoItem = mValues.get(position);
        // 将ToDoItem的数据绑定到ViewHolder的视图组件上
        holder.titleTextView.setText(toDoItem.getTitle());
//        holder.contentTextView.setText(toDoItem.getContent());
        holder.timeTextView.setText(toDoItem.getTime().toString());
//        holder.importanceTextView.setText(toDoItem.getImportance());
//        holder.isCompletedTextView.setText(toDoItem.isCompleted() ? "已完成" : "未完成");
        // 根据完成状态改变标题颜色
        if (toDoItem.isCompleted()) {
            holder.titleTextView.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.darker_gray));
        } else {
            holder.titleTextView.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.black));
        }

    }

    // 返回列表中项目（Item）的数量
    @Override
    public int getItemCount() {
        return mValues.size();
    }

    // 视图持有者（ViewHolder）类，用于包含列表项的视图组件
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // 列表项中的TextView组件，用于显示ToDoItem的标题和内容
        private TextView titleTextView;
//        private TextView contentTextView;
        // 列表项中的TextView组件，用于显示ToDoItem的时间
        private TextView timeTextView;
        // 列表项中的TextView组件，用于显示ToDoItem的重要程度
//        private TextView importanceTextView;
        // 列表项中的TextView组件，用于显示ToDoItem是否已完成
        private TextView isCompletedTextView;

        // 构造函数，使用传入的视图（view）创建ViewHolder
        public ViewHolder(View view) {
            super(view);
            // 初始化视图组件
            titleTextView = view.findViewById(R.id.text_view_title);
//            contentTextView = view.findViewById(R.id.text_view_content);
            timeTextView = view.findViewById(R.id.text_view_time);
//            importanceTextView = view.findViewById(R.id.text_view_importance);
//            isCompletedTextView = view.findViewById(R.id.text_view_is_completed);
        }
    }

    public void updateItems(List<ToDoItem> items) {
        mValues.clear(); // 清除旧的数据
        mValues.addAll(items); // 添加新的数据集
//        for (ToDoItem item : mValues) {
//            System.out.println("ToDoItem: " + item.getTitle() + ", " + item.getTime() + ", " + item.isCompleted());
//        }
        notifyDataSetChanged(); // 通知数据已更改
    }

    public void showDeleteConfirmationDialog(final int position) {
        // 创建一个确认对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("删除事项？");
        builder.setMessage("确定删除吗？");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ToDoItem itemToDelete = mValues.get(position);
                long itemId = itemToDelete.getId();
//                long itemId = 0;
                System.out.println(itemId);
                System.out.println("ToDoItem: " + itemToDelete.getId() + ", "+ itemToDelete.getTitle() + ", " + itemToDelete.getTime() + ", " + itemToDelete.isCompleted());
                // 删除数据库中的项目
                DatabaseHelper dbHelper = new DatabaseHelper(mContext);
                dbHelper.deleteTodoItem(itemId);
                System.out.println("deleteID:"+itemId);
//                printActiveNotificationIds(mContext);
//                notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
//                if (notificationManager != null) {
//                    StatusBarNotification[] activeNotifications = notificationManager.getActiveNotifications();
//                    System.out.println("Active Notification IDs:");
//                    for (StatusBarNotification statusBarNotification : activeNotifications) {
//                        int notificationId = statusBarNotification.getId();
//                        System.out.println(notificationId);
//                    }
//                }
                int notificationIdToCheck = (int) itemId; // 替换为你要检查的通知ID
                boolean isNotificationActive = isNotificationActive(mContext, notificationIdToCheck);
                if (isNotificationActive) {
                    System.out.println("Y");
                } else {
                    System.out.println("N");
                }
                notificationManager.cancel((int) itemId);
                cancelScheduledNotification((int) itemId);
                if (isNotificationActive) {
                    System.out.println("Y");
                } else {
                    System.out.println("N");
                }
                // 从数据库中重新加载最新的数据列表
                List<ToDoItem> updateItems = dbHelper.getTodoItems();
                deleteItems(updateItems);
            }
        });
        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // 取消删除，并通知适配器刷新
                notifyItemChanged(position);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void deleteItems(List<ToDoItem> items) {
        mValues.clear(); // 清除旧的数据
        mValues.addAll(items); // 添加新的数据集
//        for (ToDoItem item : mValues) {
//            System.out.println("ToDoItem: " + item.getTitle() + ", " + item.getTime() + ", " + item.isCompleted());
//        }
        notifyDataSetChanged(); // 通知数据已更改
    }

    public static boolean isNotificationActive(Context context, int notificationId) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            StatusBarNotification[] activeNotifications = notificationManager.getActiveNotifications();
            for (StatusBarNotification statusBarNotification : activeNotifications) {
                if (statusBarNotification.getId() == notificationId) {
                    return true; // 找到匹配的通知ID
                }
            }
        }
        return false; // 没有找到匹配的通知ID
    }

    private void cancelScheduledNotification(int notificationId) {
        Intent intent = new Intent(mContext, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                mContext, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT| PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }
}
