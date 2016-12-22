package com.alexstyl.specialdates.addevent;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

final class ContactEventViewHolder extends RecyclerView.ViewHolder {
    private final ImageView icon;
    private final TextView datePicker;

    ContactEventViewHolder(View view, ImageView icon, TextView datePicker) {
        super(view);
        this.icon = icon;
        this.datePicker = datePicker;
    }

    public void bind(final ContactEventViewModel viewModel, final OnEventTappedListener onEventTappedListener) {
        icon.setImageResource(viewModel.getEventIconRes());
        datePicker.setHint(viewModel.getHintText());
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEventTappedListener.onEventTapped(viewModel);
            }
        });
    }
}
