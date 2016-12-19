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

    public void bind(ContactEventViewModel viewModel) {
        icon.setImageResource(viewModel.getEventIconRes());
        datePicker.setHint(viewModel.getHintText());
    }
}
