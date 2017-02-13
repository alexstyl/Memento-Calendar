package com.alexstyl.specialdates.upcoming.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;

abstract class UpcomingRowViewHolder<T> extends RecyclerView.ViewHolder {

    UpcomingRowViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bind(T element, OnUpcomingEventClickedListener listener);
}
