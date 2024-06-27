package com.example.mytodo.recycler;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytodo.recycler.RecyclerViewAdapter;

public class MyItemTouchHelper extends ItemTouchHelper.Callback {

    private RecyclerViewAdapter adapter;

    public MyItemTouchHelper(RecyclerViewAdapter adapter) {
        adapter = adapter;
    }

    // 滑动删除
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(0, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }


    // 滑动效果
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getBindingAdapterPosition();
        adapter.showDeleteConfirmationDialog(position);
    }
}
