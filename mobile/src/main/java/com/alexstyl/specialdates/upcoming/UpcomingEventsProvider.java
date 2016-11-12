package com.alexstyl.specialdates.upcoming;

import android.support.v4.app.FragmentActivity;

import com.alexstyl.specialdates.date.CelebrationDate;
import com.alexstyl.specialdates.date.Date;

import java.util.ArrayList;
import java.util.List;

class UpcomingEventsProvider {

    private final UpcomingEventsFetcher fetcher;

    private LoadingListener listener;

    static UpcomingEventsProvider newInstance(FragmentActivity activity, LoadingListener onEventsLoadedListener) {
        UpcomingEventsFetcher upcomingEventsFetcher = new UpcomingEventsFetcher(activity.getSupportLoaderManager(), activity, Date.today());
        return new UpcomingEventsProvider(upcomingEventsFetcher, onEventsLoadedListener);
    }

    private UpcomingEventsProvider(UpcomingEventsFetcher fetcher, LoadingListener listener) {
        this.fetcher = fetcher;
        this.listener = listener;
    }

    public void reloadData() {
        fetcher.loadDatesStartingFromDate(callback);
    }

    private final UpcomingEventsFetcher.Callback callback = new UpcomingEventsFetcher.Callback() {

        private List<CelebrationDate> accumulatedDates = new ArrayList<>();

        @Override
        public void onUpcomingDatesLoaded(List<CelebrationDate> dates) {
            appendCelebrationDates(dates);
            presentDatesToListener();
        }

        private void appendCelebrationDates(List<CelebrationDate> dates) {
            accumulatedDates.clear();
            accumulatedDates.addAll(dates);
        }

        private void presentDatesToListener() {
            listener.onUpcomingEventsLoaded(accumulatedDates);
        }

    };

    interface LoadingListener {
        void onUpcomingEventsLoaded(List<CelebrationDate> dates);
    }
}
