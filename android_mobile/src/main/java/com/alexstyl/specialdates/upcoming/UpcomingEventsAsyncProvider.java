package com.alexstyl.specialdates.upcoming;

import java.util.List;

class UpcomingEventsAsyncProvider {

    private final UpcomingEventsFetcher fetcher;

    UpcomingEventsAsyncProvider(UpcomingEventsFetcher fetcher) {
        this.fetcher = fetcher;
    }

    void reloadData(final Callback listener) {
        fetcher.loadDatesStartingFromDate(new UpcomingEventsFetcher.Callback() {

            @Override
            public void onUpcomingDatesLoaded(List<UpcomingRowViewModel> dates) {
                listener.onUpcomingEventsLoaded(dates);
            }

        });
    }

    interface Callback {
        void onUpcomingEventsLoaded(List<UpcomingRowViewModel> dates);
    }
}
