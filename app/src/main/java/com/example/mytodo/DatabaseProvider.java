package com.example.mytodo;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.mytodo.DatabaseHelper;


//我发现我完全不用写这个玩意，因为没别的app调取uri
public class DatabaseProvider extends ContentProvider {

    // 定义URI匹配器，用于匹配不同的URI
    private static final int ITEMS = 1; // 匹配所有待办事项
    private static final int ITEM_ID = 2; // 匹配特定待办事项的ID

    private static final UriMatcher uriMatcher;

    // 静态块，初始化URI匹配器
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("com.example.mytodo", "items", ITEMS); // 匹配所有items
        uriMatcher.addURI("com.example.mytodo", "items/#", ITEM_ID); // 匹配特定item的ID
    }

    // 数据库帮助类实例，用于管理数据库的创建、升级和操作
    private DatabaseHelper databaseHelper;

    // 内容提供者创建时调用，用于初始化数据库帮助类
    @Override
    public boolean onCreate() {
        Context context = getContext();
        databaseHelper = new DatabaseHelper(context); // 创建DatabaseHelper实例
        return true;
    }

    // 查询操作，根据URI返回一个游标
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase(); // 获取可读数据库实例
        Cursor cursor = null;

        // 根据URI匹配器匹配到的结果，执行不同的查询操作
        switch (uriMatcher.match(uri)) {
            case ITEMS:
                // 查询所有待办事项，按时间顺序排序
                cursor = db.query("items", projection, selection, selectionArgs, null, null, "time ASC");
                break;
            case ITEM_ID:
                selection = "_id=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query("items", projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        return cursor;
    }

    // 获取内容类型，用于判断URI的类型
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case ITEMS:
                return "vnd.android.cursor.dir/com.example.mytodo.items"; // items的MIME类型
            case ITEM_ID:
                return "vnd.android.cursor.item/com.example.mytodo.items"; // 特定item的MIME类型
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    // 插入操作，将ContentValues中的数据插入到数据库
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long rowId = db.insert("items", null, values);
        if (rowId > 0) {
            Uri insertedUri = ContentUris.withAppendedId(uri, rowId);
            getContext().getContentResolver().notifyChange(insertedUri, null);
            return insertedUri;
        }
        return null;
    }

    // 更新操作，根据ContentValues更新数据库中的数据
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int count = db.update("items", values, selection, selectionArgs);
        if (count > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    // 删除操作，从数据库中删除数据
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int count = db.delete("items", selection, selectionArgs);
        if (count > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }
}

