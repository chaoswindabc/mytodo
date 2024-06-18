package com.example.mytodo;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        // 初始化数据库（如果尚未创建）
        dbHelper.getWritableDatabase();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home,
//                R.id.navigation_dashboard,
//                R.id.navigation_notifications,
                R.id.navigation_recycler
//                R.id.navigation_calendar,
//                R.id.navigation_important
        )
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

// 插入两个预设的待办事项
        dbHelper.insertTodoItem("完成作业", "2024-06-20 18:00:00", false);
        dbHelper.insertTodoItem("购物", "2024-06-21 10:00:00", false);
    }

    // 在 MainActivity 的 onDestroy 方法中添加以下代码
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        DatabaseHelper dbHelper = new DatabaseHelper(this);
//        // 删除两个预设的待办事项
//        dbHelper.deleteTodoItem("完成作业");
//        dbHelper.deleteTodoItem("购物");
//    }

}