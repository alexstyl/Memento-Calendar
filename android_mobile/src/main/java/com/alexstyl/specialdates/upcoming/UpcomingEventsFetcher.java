package com.alexstyl.specialdates.upcoming;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.alexstyl.resources.AndroidColorResources;
import com.alexstyl.resources.StringResources;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.donate.DonationPreferences;
import com.alexstyl.specialdates.events.bankholidays.BankHolidayProvider;
import com.alexstyl.specialdates.events.bankholidays.BankHolidaysPreferences;
import com.alexstyl.specialdates.events.bankholidays.GreekBankHolidaysCalculator;
import com.alexstyl.specialdates.events.namedays.NamedayPreferences;
import com.alexstyl.specialdates.events.namedays.calendar.OrthodoxEasterCalculator;
import com.alexstyl.specialdates.events.namedays.calendar.resource.NamedayCalendarProvider;
import com.alexstyl.specialdates.service.PeopleEventsProvider;
import com.alexstyl.specialdates.upcoming.widget.list.NoAds;
import com.alexstyl.specialdates.upcoming.widget.list.UpcomingEventsProvider;
import com.novoda.notils.exception.DeveloperError;

import java.util.List;
import java.util.Locale;

class UpcomingEventsFetcher {

    private static final int LOADER_ID_DATES = 2;

    private final LoaderManager loaderManager;
    private final Date startingDate;
    private final Context context;
    private final StringResources stringResources;
    private Callback callback;

    UpcomingEventsFetcher(LoaderManager loaderManager, Context context, Date startingDate, StringResources stringResources) {
        this.loaderManager = loaderManager;
        this.context = context;
        this.startingDate = startingDate;
        this.stringResources = stringResources;
    }

    void loadDatesStartingFromDate(Callback callback) {
        this.callback = callback;
        this.loaderManager.restartLoader(LOADER_ID_DATES, null, loaderCallbacks);
    }

    private final LoaderManager.LoaderCallbacks<List<UpcomingRowViewModel>> loaderCallbacks = new LoaderManager.LoaderCallbacks<List<UpcomingRowViewModel>>() {

        @Override
        public Loader<List<UpcomingRowViewModel>> onCreateLoader(int loaderID, Bundle arg1) {
            if (loaderID == LOADER_ID_DATES) {
                PeopleEventsProvider peopleEventsProvider = PeopleEventsProvider.newInstance(context);
                NamedayCalendarProvider namedayCalendarProvider = NamedayCalendarProvider.newInstance(context.getResources());
                AndroidColorResources colorResources = new AndroidColorResources(context.getResources());

                UpcomingEventsAdRules adRules = DonationPreferences.newInstance(context).hasDonated() ? new NoAds() : new UpcomingEventsFreeUserAdRules();

                return new UpcomingEventsLoader(
                        context,
                        startingDate,
                        new UpcomingEventsProvider(
                                peopleEventsProvider,
                                NamedayPreferences.newInstance(context),
                                BankHolidaysPreferences.newInstance(context),
                                new BankHolidayProvider(new GreekBankHolidaysCalculator(OrthodoxEasterCalculator.INSTANCE)),
                                namedayCalendarProvider,
                                new UpcomingEventRowViewModelFactory(
                                        startingDate,
                                        new UpcomingDateStringCreator(stringResources, startingDate),
                                        new ContactViewModelFactory(colorResources, stringResources),
                                        stringResources,
                                        new BankHolidayViewModelFactory(),
                                        new NamedaysViewModelFactory(startingDate),
                                        MonthLabels.forLocale(Locale.getDefault())
                                ),
                                adRules
                        )
                );
            }
            throw new DeveloperError("Unhandled loaderID: " + loaderID);
        }

        @Override
        public void onLoadFinished(Loader<List<UpcomingRowViewModel>> loader, List<UpcomingRowViewModel> celebrationDates) {
            if (loader.getId() == LOADER_ID_DATES) {
                callback.onUpcomingDatesLoaded(celebrationDates);
            }
        }

        @Override
        public void onLoaderReset(Loader<List<UpcomingRowViewModel>> loader) {
            // no-op
        }
    };

    interface Callback {
        void onUpcomingDatesLoaded(List<UpcomingRowViewModel> dates);

    }
}
