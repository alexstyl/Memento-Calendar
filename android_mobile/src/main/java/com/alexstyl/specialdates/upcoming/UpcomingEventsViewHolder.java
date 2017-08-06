package com.alexstyl.specialdates.upcoming;

import android.view.View;
import android.widget.TextView;

import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.upcoming.view.OnUpcomingEventClickedListener;
import com.alexstyl.specialdates.upcoming.view.UpcomingEventsView;

class UpcomingEventsViewHolder extends UpcomingRowViewHolder<UpcomingEventsViewModel> {

    private final TextView dateView;
    private final UpcomingEventsView eventsView;
    private final ImageLoader imageLoader;

    UpcomingEventsViewHolder(View view,
                             TextView titleView,
                             UpcomingEventsView upcomingEventsView,
                             ImageLoader imageLoader) {
        super(view);
        this.dateView = titleView;
        this.eventsView = upcomingEventsView;
        this.imageLoader = imageLoader;
    }

    @Override
    public void bind(final UpcomingEventsViewModel viewModel, final OnUpcomingEventClickedListener listener) {
        dateView.setText(viewModel.getDisplayDateLabel());
        dateView.setTypeface(viewModel.getDateTypeFace());

        eventsView.bind(viewModel, listener, imageLoader);
    }
}
