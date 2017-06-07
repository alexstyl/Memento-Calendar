package com.alexstyl.specialdates.datedetails;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.images.ImageLoader;

class CompactSizeViewHolderFactory implements FlexibleSizeViewHolderFactory {
    private final LayoutInflater layoutInflater;
    private final ImageLoader imageLoader;

    CompactSizeViewHolderFactory(LayoutInflater layoutInflater, ImageLoader imageLoader) {
        this.layoutInflater = layoutInflater;
        this.imageLoader = imageLoader;
    }

    @Override
    public CompactDateDetailsViewHolder createContactEventViewHolder(ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.base_card_compact, parent, false);
        return new CompactDateDetailsViewHolder(view, imageLoader);
    }

}
