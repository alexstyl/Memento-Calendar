package com.alexstyl.specialdates.people;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

class PeopleItemDecorator extends RecyclerView.ItemDecoration {

    private final int categoryMargin;
    private final int itemInBetweenMargin;

    PeopleItemDecorator(int categoryMargin, int itemInBetweenMargin) {
        this.categoryMargin = categoryMargin;
        this.itemInBetweenMargin = itemInBetweenMargin;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildViewHolder(view) instanceof ImportFromFacebookViewHolder) {
            outRect.bottom = categoryMargin;
        } else {
            outRect.bottom = itemInBetweenMargin;
        }

    }
}
