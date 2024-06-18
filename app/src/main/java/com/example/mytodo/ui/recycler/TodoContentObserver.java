package com.example.mytodo.ui.recycler;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;

import com.example.mytodo.ui.recycler.RecyclerViewAdapter;

public class TodoContentObserver extends ContentObserver {

    private RecyclerViewAdapter adapter;

    public TodoContentObserver(Handler handler, RecyclerViewAdapter adapter) {
        super(handler);
        this.adapter = adapter;
    }

    // 当数据库内容发生变化时，onChange方法会被调用
    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}
