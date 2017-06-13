package com.alexstyl.specialdates.datedetails;

import android.support.v7.widget.RecyclerView;
import android.view.View;

abstract class DateDetailsViewHolder<T extends DateDetailsViewModel> extends RecyclerView.ViewHolder {

    DateDetailsViewHolder(View itemView) {
        super(itemView);
    }

    abstract void bind(T viewModel, DateDetailsClickListener listener);
}
