package com.alexstyl.specialdates.addevent;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexstyl.android.ViewVisibility;

final class ContactEventViewHolder extends RecyclerView.ViewHolder {
    private final ImageView icon;
    private final TextView datePicker;
    private final ImageButton removeEvent;

    ContactEventViewHolder(View view, ImageView icon, TextView datePicker, ImageButton removeEvent) {
        super(view);
        this.icon = icon;
        this.datePicker = datePicker;
        this.removeEvent = removeEvent;
    }

    public void bind(final AddEventContactEventViewModel viewModel, final ContactDetailsListener contactDetailsListener) {
        icon.setImageResource(viewModel.getEventIconRes());
        datePicker.setHint(viewModel.getHintText());
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactDetailsListener.onAddEventClicked(viewModel);
            }
        });
        @ViewVisibility int clearVisibility = viewModel.getClearVisibility();
        removeEvent.setVisibility(clearVisibility);
        removeEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactDetailsListener.onRemoveEventClicked(viewModel.getEventType());
            }
        });
    }
}
