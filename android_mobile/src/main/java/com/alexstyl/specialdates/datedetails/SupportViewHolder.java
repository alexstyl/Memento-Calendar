package com.alexstyl.specialdates.datedetails;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

class SupportViewHolder extends DateDetailsViewHolder<SupportViewModel> {

    private final View view;
    private final ImageView heartView;
    private final TextView textDescription;

    SupportViewHolder(View view, ImageView heartView, TextView textDescription) {
        super(view);
        this.view = view;
        this.heartView = heartView;
        this.textDescription = textDescription;
    }

    @Override
    void bind(SupportViewModel viewModel, final DateDetailsClickListener listener) {
        heartView.startAnimation(viewModel.getHeartAnimation());
        textDescription.append(viewModel.getDescription());
        view.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onSupportCardClicked();
                    }
                }
        );
    }
}
