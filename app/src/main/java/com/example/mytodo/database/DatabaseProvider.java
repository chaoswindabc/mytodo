package com.example.mytodo.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

// 这段直接用chatglm了
// 我后来发现我完全不用写这个玩意，因为根本没别的app调取uri
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

    private DatabaseHelper databaseHelper;

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

        switch (uriMatcher.match(uri)) {
            case ITEMS:
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

    // 插入操作
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

    // 更新操作
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        int count = db.update("items", values, selection, selectionArgs);
        if (count > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    // 删除操作
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

