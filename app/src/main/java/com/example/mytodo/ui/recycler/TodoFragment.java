package com.example.mytodo.ui.recycler;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mytodo.R;
import com.example.mytodo.ToDoItem;
import com.example.mytodo.ui.recycler.placeholder.PlaceholderContent;

/**
 * A fragment representing a list of Items.
 */
public class TodoFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private TodoContentObserver contentObserver;
    private RecyclerViewAdapter recyclerViewAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TodoFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static TodoFragment newInstance(int columnCount) {
        TodoFragment fragment = new TodoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo_recycler, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
//            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
//            } else {
//                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
//            }
            recyclerViewAdapter = new RecyclerViewAdapter(ToDoItem.getItems()); // 使用ToDoItem的getItems()方法
            recyclerView.setAdapter(recyclerViewAdapter);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Register the content observer
        contentObserver = new TodoContentObserver(new Handler(), recyclerViewAdapter); // 创建TodoContentObserver实例并传入adapter
        requireContext().getContentResolver().registerContentObserver(
                Uri.parse("content://com.example.mytodo/items"),
                true,
                contentObserver); // 注册contentObserver
    }

    @Override
    public void onPause() {
        super.onPause();
        // Unregister the content observer
        if (contentObserver != null) {
            requireContext().getContentResolver().unregisterContentObserver(contentObserver); // 注销contentObserver
        }
    }
}
