package com.alexstyl.specialdates.upcoming;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

class UpcomingEventsDecorator extends RecyclerView.ItemDecoration {

    private final int spacingFromHeaders;
    private final int itemsInBetweenSpacing;

    UpcomingEventsDecorator(int spacingFromHeaders, int itemsInBetweenSpacing) {
        this.spacingFromHeaders = spacingFromHeaders;
        this.itemsInBetweenSpacing = itemsInBetweenSpacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        RecyclerView.ViewHolder childViewHolder = parent.getChildViewHolder(view);
        if (isAHeaderItem(childViewHolder) && isNotFirstItem(childViewHolder)) {
            outRect.top = spacingFromHeaders;
        } else {
            outRect.top = itemsInBetweenSpacing;
        }
    }

    private boolean isAHeaderItem(RecyclerView.ViewHolder childViewHolder) {
        return childViewHolder instanceof DateHeaderViewHolder;
    }

    private boolean isNotFirstItem(RecyclerView.ViewHolder childViewHolder) {
        return childViewHolder.getLayoutPosition() != 0;
    }
}
