package com.alexstyl.specialdates.datedetails;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.ui.widget.ColorImageView;
import com.novoda.notils.caster.Views;

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
        LinearLayout actions = Views.findById(view, R.id.card_actions);
        TextView displayName = Views.findById(view, R.id.search_result_contact_name);
        TextView eventLabel = Views.findById(view, R.id.event_label);
        ColorImageView avatar = Views.findById(view, R.id.search_result_avatar);
        return new DetailedDateDetailsViewHolder(view, imageLoader, avatar, displayName, eventLabel, actions);
    }

}
