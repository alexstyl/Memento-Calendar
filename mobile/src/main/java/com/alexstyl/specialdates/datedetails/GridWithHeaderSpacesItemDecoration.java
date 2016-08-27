package com.alexstyl.specialdates.datedetails;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

class GridWithHeaderSpacesItemDecoration extends RecyclerView.ItemDecoration {

    private int columnCount = 1;
    private int halfSpacing;
    private int spacing;
    private DateDetailsAdapter adapter;
    private int numberOfHeaders;

    public GridWithHeaderSpacesItemDecoration(int size, DateDetailsAdapter adapter) {
        this.spacing = size;
        this.adapter = adapter;
        this.halfSpacing = size / 2;
    }

    public void setNumberOfColumns(int columnCount) {
        this.columnCount = columnCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        // Add top margin only for the first item to avoid double space between items
        int itemPos = parent.getChildLayoutPosition(view);
        if (!isHeader(itemPos)) {
            if (columnCount != 1) { // don't care about left right spaces
                if (isRightSide(itemPos)) {
                    outRect.right = halfSpacing;
                } else {
                    outRect.left = halfSpacing;
                }

            }
        }
        outRect.bottom = spacing;

    }

    private boolean isRightSide(int itemPos) {
        return itemPos % columnCount == adapter.getHeaderCount();
    }

    private boolean isHeader(int itemPos) {
        return adapter.isFullRowAt(itemPos);
    }

    private boolean isTopRow(int pos) {
        if (isHeader(pos)) {
            return true;
        }
        if (pos / columnCount == 0) {
            return true;
        }
        return false;
    }
}
