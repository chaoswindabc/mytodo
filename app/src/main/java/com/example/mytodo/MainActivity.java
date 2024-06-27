package com.example.mytodo;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.mytodo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private static final String CHANNEL_ID = "todo_channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        // 初始化数据库（如果尚未创建）
        dbHelper.getWritableDatabase();

        createNotificationChannel();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home,
//                R.id.navigation_dashboard,
                R.id.navigation_recycler,
                R.id.navigation_notifications
//                R.id.navigation_calendar,
//                R.id.navigation_important
        )
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

// 插入两个预设的待办事项
//        dbHelper.insertTodoItem("完成作业", "2024-06-20 18:00:00", false);
//        dbHelper.insertTodoItem("购物", "2024-06-21 10:00:00", false);

        // 删除两个预设的待办事项
//        dbHelper.deleteTodoItem("完成作业");
//        dbHelper.deleteTodoItem("购物");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        DatabaseHelper dbHelper = new DatabaseHelper(this);
//        // 删除两个预设的待办事项
//        dbHelper.deleteTodoItem("完成作业");
//        dbHelper.deleteTodoItem("购物");
//        this.deleteDatabase("todo_list.db");
    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "ToDoChannel";
            String description = "notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}