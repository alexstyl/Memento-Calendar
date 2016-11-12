package com.alexstyl.specialdates.upcoming;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.alexstyl.specialdates.date.CelebrationDate;
import com.alexstyl.specialdates.service.PeopleEventsProvider;
import com.novoda.notils.exception.DeveloperError;

import java.util.List;

class UpcomingEventsFetcher {

    private static final String KEY_LOADING_TIME = "alexstyl:loading_time";
    private static final int LOADER_ID_DATES = 2;

    private final LoaderManager loaderManager;
    private Callback callback;
    private final Context context;

    UpcomingEventsFetcher(LoaderManager loaderManager, Context context) {
        this.loaderManager = loaderManager;
        this.context = context;
    }

    public void loadDatesBetween(TimePeriod duration, Callback callback) {
        this.callback = callback;
        Bundle args = new Bundle();
        args.putSerializable(KEY_LOADING_TIME, duration);
        this.loaderManager.restartLoader(LOADER_ID_DATES, args, loaderCallbacks);
    }

    private final LoaderManager.LoaderCallbacks<List<CelebrationDate>> loaderCallbacks = new LoaderManager.LoaderCallbacks<List<CelebrationDate>>() {

        @Override
        public Loader<List<CelebrationDate>> onCreateLoader(int loaderID, Bundle arg1) {
            TimePeriod duration = (TimePeriod) arg1.getSerializable(KEY_LOADING_TIME);
            if (loaderID == LOADER_ID_DATES) {
                return new UpcomingEventsLoader(context, PeopleEventsProvider.newInstance(context), duration);
            }
            throw new DeveloperError("Unhandled loaderID: " + loaderID);
        }

        @Override
        public void onLoadFinished(Loader<List<CelebrationDate>> loader, List<CelebrationDate> celebrationDates) {
            if (loader.getId() == LOADER_ID_DATES) {
                callback.onUpcomingDatesLoaded(celebrationDates);
            }
        }

        @Override
        public void onLoaderReset(Loader<List<CelebrationDate>> loader) {
            // no-op
        }
    };

    interface Callback {
        void onUpcomingDatesLoaded(List<CelebrationDate> dates);

    }
}
