package com.alexstyl.specialdates.datedetails;

import android.graphics.Rect;
import android.support.annotation.Px;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import static android.R.attr.columnCount;

class EventsSpacingDecoration extends RecyclerView.ItemDecoration {

    private final int halfSpacing;
    private final int spacing;
    private final DateDetailsAdapter adapter;
    private final DateDetailsSpanLookup spanSizeLookup;
    private int numberOfColumns;

    EventsSpacingDecoration(@Px int spacing, DateDetailsAdapter adapter, DateDetailsSpanLookup spanSizeLookup) {
        this.spacing = spacing;
        this.halfSpacing = spacing / 2;
        this.adapter = adapter;
        this.spanSizeLookup = spanSizeLookup;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildLayoutPosition(view);
        if (numberOfColumns > 1) {
            if (isNotFullRow(position)) {
                if (isRightSide(position)) {
                    outRect.right = halfSpacing;
                } else {
                    outRect.left = halfSpacing;
                }
            }
        }

        outRect.bottom = spacing;

    }

    private boolean isRightSide(int position) {
        return position % columnCount == adapter.getHeaderCount();
    }

    private boolean isNotFullRow(int itemPos) {
        return spanSizeLookup.getSpanSize(itemPos) != DateDetailsSpanLookup.FULL_SPAN;
    }

    void setNumberOfColumns(int numberOfColumns) {
        this.numberOfColumns = numberOfColumns;
    }
}
