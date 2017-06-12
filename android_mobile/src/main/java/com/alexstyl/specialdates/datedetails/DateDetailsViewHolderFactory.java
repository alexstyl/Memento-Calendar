package com.alexstyl.specialdates.datedetails;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.images.ImageLoader;
import com.novoda.notils.caster.Views;

final class DateDetailsViewHolderFactory {

    private final LayoutInflater layoutInflater;
    private final FlexibleSizeViewHolderFactory factory;

    static DateDetailsViewHolderFactory createDetailedFactory(LayoutInflater layoutInflater, ImageLoader imageLoader) {
        CardActionFactory factory = new CardActionFactory(layoutInflater);
        return new DateDetailsViewHolderFactory(layoutInflater, new DetailedSizeViewHolderFactory(layoutInflater, imageLoader, factory));
    }

    static DateDetailsViewHolderFactory createCompactFactory(LayoutInflater layoutInflater, ImageLoader imageLoader) {
        return new DateDetailsViewHolderFactory(layoutInflater, new CompactSizeViewHolderFactory(layoutInflater, imageLoader));
    }

    private DateDetailsViewHolderFactory(LayoutInflater layoutInflater, FlexibleSizeViewHolderFactory factory) {
        this.layoutInflater = layoutInflater;
        this.factory = factory;
    }

    public DateDetailsViewHolder createFor(int viewType, ViewGroup parent) {
        if (viewType == DateDetailsViewType.CONTACT_EVENT) {
            return factory.createContactEventViewHolder(parent);
        }
        if (viewType == DateDetailsViewType.NAMEDAY) {
            View view = layoutInflater.inflate(R.layout.card_namedays, parent, false);
            NamedayCardView namedayCard = Views.findById(view, R.id.nameday_card);
            return new NameDayCardViewHolder(view, namedayCard);
        }
        if (viewType == DateDetailsViewType.BANKHOLIDAY) {
            View view = layoutInflater.inflate(R.layout.card_bankholiday, parent, false);
            return new BankViewHolder(view);
        }
        if (viewType == DateDetailsViewType.RATE_APP) {
            View view = layoutInflater.inflate(R.layout.card_full_support_heart, parent, false);
            ImageView heartView = Views.findById(view, R.id.support_card_heroimage);
            TextView textDescription = Views.findById(view, R.id.support_card_description);
            return new SupportViewHolder(view, heartView, textDescription);
        }
        throw new IllegalStateException("Invalid viewType " + viewType);
    }
}
