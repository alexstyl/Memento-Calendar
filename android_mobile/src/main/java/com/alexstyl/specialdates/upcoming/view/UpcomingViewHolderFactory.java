package com.alexstyl.specialdates.upcoming.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.upcoming.UpcomingRowViewType;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.NativeExpressAdView;
import com.novoda.notils.caster.Views;
import com.novoda.notils.exception.DeveloperError;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

final class UpcomingViewHolderFactory {

    private final LayoutInflater layoutInflater;
    private final ImageLoader imageLoader;

    UpcomingViewHolderFactory(LayoutInflater layoutInflater, ImageLoader imageLoader) {
        this.layoutInflater = layoutInflater;
        this.imageLoader = imageLoader;
    }

    public UpcomingRowViewHolder createFor(int viewType, ViewGroup parent) {
        if (viewType == UpcomingRowViewType.UPCOMING_EVENTS) {
            View view = layoutInflater.inflate(R.layout.row_upcoming_day, parent, false);
            TextView titleView = Views.findById(view, R.id.upcoming_events_title);
            UpcomingEventsView eventsView = Views.findById(view, R.id.upcoming_events_view);
            return new UpcomingEventsViewHolder(view, titleView, eventsView, imageLoader);
        } else if (viewType == UpcomingRowViewType.MONTH) {
            View view = layoutInflater.inflate(R.layout.row_header, parent, false);
            TextView monthView = (TextView) view.findViewById(R.id.upcoming_event_header);
            return new MonthViewHolder(view, monthView);
        } else if (viewType == UpcomingRowViewType.YEAR) {
            View view = layoutInflater.inflate(R.layout.row_header, parent, false);
            TextView headerView = (TextView) view.findViewById(R.id.upcoming_event_header);
            return new YearViewHolder(view, headerView);
        } else if (viewType == UpcomingRowViewType.AD) {
            NativeExpressAdView adView = new NativeExpressAdView(parent.getContext());
            adView.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
            adView.setAdSize(new AdSize(AdSize.FULL_WIDTH, 132));
            adView.setAdUnitId(parent.getResources().getString(R.string.admob_unit_id));
            return new AdViewHolder(adView, adView);
        } else {
            throw new DeveloperError("Invalid viewType " + viewType);
        }
    }
}
