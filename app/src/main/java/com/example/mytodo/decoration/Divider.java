package com.example.mytodo.decoration;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class Divider extends RecyclerView.ItemDecoration {
    private Drawable divider;

    public Divider(Drawable divider) {
        // 利用Drawable绘制分割线
        divider = divider;
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        // 计算需要绘制的区域
        Rect rect = new Rect();
        for (int i = 0; i < childCount; i++) {
            View childView = parent.getChildAt(i);
            // 设置分隔线的左侧位置
            rect.left = parent.getPaddingLeft();
            // 设置分隔线的右侧位置
            rect.right = parent.getWidth() - parent.getPaddingRight();
            // 设置分隔线的顶部位置，即子视图的底部
            rect.top = childView.getBottom();
            // 设置分隔线的底部位置，即顶部加上分隔线的高度
            rect.bottom = rect.top + divider.getIntrinsicHeight();
            // 直接利用Canvas去绘制分隔线
            divider.setBounds(rect);
            divider.draw(canvas);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        // 在每个子View的下面留出来画分割线
        outRect.bottom += divider.getIntrinsicHeight();
    }

}
