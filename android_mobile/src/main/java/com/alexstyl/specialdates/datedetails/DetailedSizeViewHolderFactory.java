package com.alexstyl.specialdates.datedetails;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.images.ImageLoader;

class DetailedSizeViewHolderFactory implements FlexibleSizeViewHolderFactory {
    private final LayoutInflater layoutInflater;
    private final ImageLoader imageLoader;

    DetailedSizeViewHolderFactory(LayoutInflater layoutInflater, ImageLoader imageLoader) {
        this.layoutInflater = layoutInflater;
        this.imageLoader = imageLoader;
    }

    @Override
    public DateDetailsViewHolder createContactEventViewHolder(ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.card_contact_event_full, parent, false);
        return new DetailedDateDetailsViewHolder(view, imageLoader);
    }

}
