package com.alexstyl.specialdates.upcoming.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.upcoming.UpcomingRowViewType;
import com.alexstyl.specialdates.upcoming.view.UpcomingEventsView;
import com.novoda.notils.caster.Views;
import com.novoda.notils.exception.DeveloperError;

public final class UpcomingViewHolderFactory {

    private final LayoutInflater layoutInflater;
    private final ImageLoader imageLoader;

    public UpcomingViewHolderFactory(LayoutInflater layoutInflater, ImageLoader imageLoader) {
        this.layoutInflater = layoutInflater;
        this.imageLoader = imageLoader;
    }

    public UpcomingRowViewHolder createFor(int viewType, ViewGroup parent) {
        if (viewType == UpcomingRowViewType.UPCOMING_EVENTS) {
            View view = layoutInflater.inflate(R.layout.row_upcoming_day, parent, false);
            TextView titleView = Views.findById(view, R.id.upcoming_events_title);
            UpcomingEventsView eventsView = Views.findById(view, R.id.upcoming_events_view);
            return new UpcomingEventsViewHolder(view, titleView, eventsView, imageLoader);
        } else if (viewType == UpcomingRowViewType.MONTH_HEADER) {
            View view = layoutInflater.inflate(R.layout.row_header_month, parent, false);
            TextView monthView = (TextView) view.findViewById(android.R.id.text1);
            return new MonthHeaderViewHolder(view, monthView);
        } else {
            throw new DeveloperError("Invalid viewType " + viewType);
        }
    }
}
