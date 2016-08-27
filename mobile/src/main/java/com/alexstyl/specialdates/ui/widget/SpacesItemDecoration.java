package com.alexstyl.specialdates.ui.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    final private int space;
    private final int columns;

    public SpacesItemDecoration(int space, int columns) {
        this.space = space;
        this.columns = columns;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {

        outRect.bottom = space;

        if (parent.getChildLayoutPosition(view) < columns) {
            outRect.top = space;
        }

    }
}
