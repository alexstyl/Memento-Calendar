package com.alexstyl.specialdates.datedetails;

import android.view.View;

import com.alexstyl.specialdates.images.ImageLoader;

class DetailedDateDetailsViewHolder extends DateDetailsViewHolder<ContactEventViewModel> {

    private final ImageLoader imageLoader;

    DetailedDateDetailsViewHolder(View itemView, ImageLoader imageLoader) {
        super(itemView);
        this.imageLoader = imageLoader;
    }

    @Override
    void bind(ContactEventViewModel viewModel, DateDetailsClickListener listener) {

    }
}
