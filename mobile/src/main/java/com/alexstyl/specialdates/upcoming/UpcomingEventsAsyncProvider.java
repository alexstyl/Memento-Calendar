package com.alexstyl.specialdates.upcoming;

import android.support.v4.app.FragmentActivity;

import com.alexstyl.specialdates.date.Date;

import java.util.List;

class UpcomingEventsAsyncProvider {

    private final UpcomingEventsFetcher fetcher;

    private LoadingListener listener;

    static UpcomingEventsAsyncProvider newInstance(FragmentActivity activity, LoadingListener onEventsLoadedListener) {
        UpcomingEventsFetcher upcomingEventsFetcher = new UpcomingEventsFetcher(activity.getSupportLoaderManager(), activity, Date.today());
        return new UpcomingEventsAsyncProvider(upcomingEventsFetcher, onEventsLoadedListener);
    }

    private UpcomingEventsAsyncProvider(UpcomingEventsFetcher fetcher, LoadingListener listener) {
        this.fetcher = fetcher;
        this.listener = listener;
    }

    void reloadData() {
        fetcher.loadDatesStartingFromDate(callback);
    }

    private final UpcomingEventsFetcher.Callback callback = new UpcomingEventsFetcher.Callback() {

        @Override
        public void onUpcomingDatesLoaded(List<UpcomingRowViewModel> dates) {
            listener.onUpcomingEventsLoaded(dates);
        }

    };

    interface LoadingListener {
        void onUpcomingEventsLoaded(List<UpcomingRowViewModel> dates);
    }
}
