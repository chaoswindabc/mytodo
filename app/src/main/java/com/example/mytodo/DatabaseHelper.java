package com.example.mytodo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// 数据库帮助类，用于管理SQLite数据库的创建、升级和操作
public class DatabaseHelper extends SQLiteOpenHelper {
    // 数据库名称
    private static final String DATABASE_NAME = "todo_list.db";
    // 数据库版本
    private static final int DATABASE_VERSION = 1;

    // 创建数据库和表的SQL语句
    private static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS items (" +
                    "    id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "    title TEXT NOT NULL, " +
                    "    content TEXT, " +
                    "    time DATETIME NOT NULL, " +
                    "    importance TEXT, " +
                    "    is_completed BOOLEAN NOT NULL DEFAULT FALSE " +
                    ");";

    // 构造函数，初始化数据库帮助类
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // 数据库创建时调用，用于创建表
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    // 数据库版本更新时调用，用于处理表的升级
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 在这里处理数据库升级逻辑，例如创建新的表或修改现有表
    }
}
