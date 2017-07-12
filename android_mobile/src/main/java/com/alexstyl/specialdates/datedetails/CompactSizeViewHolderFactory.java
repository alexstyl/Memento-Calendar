package com.alexstyl.specialdates.datedetails;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.ui.widget.ColorImageView;
import com.novoda.notils.caster.Views;

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
        TextView displayName = Views.findById(view, R.id.search_result_contact_name);
        TextView eventLabel = Views.findById(view, R.id.event_label);
        ColorImageView avatar = Views.findById(view, R.id.search_result_avatar);
        View actions = Views.findById(view, R.id.more_actions);
        return new CompactDateDetailsViewHolder(view, imageLoader, avatar, displayName, eventLabel, actions);
    }

}
