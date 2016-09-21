package com.alexstyl.specialdates.upcoming;

import android.support.v4.app.FragmentActivity;

import com.alexstyl.specialdates.date.CelebrationDate;
import com.alexstyl.specialdates.date.DayDate;

import java.util.ArrayList;
import java.util.List;

public class UpcomingEventsProvider {

    private final UpcomingEventsFetcher fetcher;
    private final LoadingTimeDuration displayingDuration;

    private LoadingListener listener;

    static UpcomingEventsProvider newInstance(FragmentActivity activity, LoadingListener onEventsLoadedListener) {
        UpcomingEventsFetcher upcomingEventsFetcher = new UpcomingEventsFetcher(activity.getSupportLoaderManager(), activity);
        return new UpcomingEventsProvider(upcomingEventsFetcher, startingTimeDuration(), onEventsLoadedListener);
    }

    public UpcomingEventsProvider(UpcomingEventsFetcher fetcher, LoadingTimeDuration initialDuration, LoadingListener listener) {
        this.fetcher = fetcher;
        this.displayingDuration = initialDuration;
        this.listener = listener;
    }

    private static LoadingTimeDuration startingTimeDuration() {
        int year = DayDate.todaysYear();
        DayDate startOfLastMonth = DayDate.newInstance(1, 1, year);
        DayDate endingOfNextMonth = DayDate.newInstance(31, 12, year);
        return new LoadingTimeDuration(startOfLastMonth, endingOfNextMonth);
    }

    public void reloadData() {
        fetcher.loadDatesBetween(displayingDuration, callback);
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
