package com.example.mytodo.ui.recycler;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mytodo.DatabaseHelper;
import com.example.mytodo.MyItemTouchHelper;
import com.example.mytodo.R;
import com.example.mytodo.ToDoItem;
import com.example.mytodo.ui.decoration.Divider;
import com.example.mytodo.ui.operation.AddTodoFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class TodoFragment extends Fragment implements AddTodoFragment.OnTodoItemAddedListener {

//    private static final String ARG_COLUMN_COUNT = "column-count";
//    private int mColumnCount = 1;
//    private TodoContentObserver contentObserver;
    private RecyclerViewAdapter recyclerViewAdapter;
    private Context mContext = getContext();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TodoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (getArguments() != null) {
//            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo_recycler, container, false);

        // 获取 RecyclerView 的引用
        RecyclerView recyclerView = view.findViewById(R.id.list);
        // 设置 RecyclerView 的布局管理器
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
        List<ToDoItem> todoItems = dbHelper.getTodoItems();

        // 为 RecyclerView 添加分隔线装饰器
        Drawable divider = ContextCompat.getDrawable(requireContext(), R.drawable.divider);
        RecyclerView.ItemDecoration itemDecoration = new Divider(divider);
        recyclerView.addItemDecoration(itemDecoration);

        recyclerViewAdapter = new RecyclerViewAdapter(getContext(),todoItems);
        recyclerView.setAdapter(recyclerViewAdapter);

        ItemTouchHelper.Callback callback = new MyItemTouchHelper(recyclerViewAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        FloatingActionButton add_todo = view.findViewById(R.id.add_todo);

        // 设置点击监听器
        add_todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 添加新的待做事项
                addNewTodoItem();
                recyclerViewAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
//        DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
//        List<ToDoItem> updatedItems = dbHelper.getTodoItems();
//        System.out.println("ToDo");
//        for (ToDoItem item : updatedItems) {
//            System.out.println("ToDoItem: " + item.getTitle() + ", " + item.getTime() + ", " + item.isCompleted());
//        }
//        recyclerViewAdapter.updateItems(updatedItems);
//        recyclerViewAdapter.notifyDataSetChanged();
//        // Register the content observer
////        contentObserver = new TodoContentObserver(new Handler(), recyclerViewAdapter); // 创建TodoContentObserver实例并传入adapter
//        requireContext().getContentResolver().registerContentObserver(
//                Uri.parse("content://com.example.mytodo/items"),
//                true,
////                contentObserver); // 注册contentObserver
    }

    @Override
    public void onPause() {
        super.onPause();
        // Unregister the content observer
//        if (contentObserver != null) {
//            requireContext().getContentResolver().unregisterContentObserver(contentObserver); // 注销contentObserver
//        }
    }

    public void addNewTodoItem() {
        DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
        // 创建 AddTodoFragment 的实例
        AddTodoFragment addTodoFragment = new AddTodoFragment(getContext(), dbHelper);
        addTodoFragment.setOnTodoItemAddedListener(this);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.nav_host_fragment_activity_main, addTodoFragment);
        fragmentTransaction.addToBackStack(null); // 这允许用户点击返回键时返回上一个 Fragment
        // 提交事务
        fragmentTransaction.commit();
    }


    @Override
    public void onTodoItemAdded() {
        // 当待办事项被添加时更新列表
        DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
        List<ToDoItem> updatedItems = dbHelper.getTodoItems();
        System.out.println("ToDo");
        for (ToDoItem item : updatedItems) {
            System.out.println("ToDoItem: " + item.getTitle() + ", " + item.getTime() + ", " + item.isCompleted());
        }
        recyclerViewAdapter.updateItems(updatedItems);
        recyclerViewAdapter.notifyDataSetChanged();
    }

}


