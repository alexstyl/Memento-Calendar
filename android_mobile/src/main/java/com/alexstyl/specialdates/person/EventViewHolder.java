package com.alexstyl.specialdates.person;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

class EventViewHolder extends RecyclerView.ViewHolder {
    private final TextView eventNameView;
    private final TextView eventDateView;

    EventViewHolder(View itemView, TextView eventNameView, TextView eventDateView) {
        super(itemView);
        this.eventNameView = eventNameView;
        this.eventDateView = eventDateView;
    }

    public void bind(final ContactEventViewModel viewModel, final EventPressedListener listener) {
        eventDateView.setText(viewModel.getDateLabel());
        eventNameView.setText(viewModel.getEvenName());
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onEventPressed(viewModel.getDate());
            }
        });
    }
}
