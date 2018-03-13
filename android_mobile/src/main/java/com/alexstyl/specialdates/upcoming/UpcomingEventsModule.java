package com.alexstyl.specialdates.upcoming;

import android.content.Context;

import com.alexstyl.resources.Colors;
import com.alexstyl.specialdates.CrashAndErrorTracker;
import com.alexstyl.specialdates.Strings;
import com.alexstyl.specialdates.UpcomingEventsJobCreator;
import com.alexstyl.specialdates.analytics.Analytics;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.bankholidays.AndroidBankHolidaysPreferences;
import com.alexstyl.specialdates.events.bankholidays.BankHolidayProvider;
import com.alexstyl.specialdates.events.bankholidays.GreekBankHolidaysCalculator;
import com.alexstyl.specialdates.events.namedays.NamedayUserSettings;
import com.alexstyl.specialdates.events.namedays.calendar.OrthodoxEasterCalculator;
import com.alexstyl.specialdates.events.namedays.calendar.resource.NamedayCalendarProvider;
import com.alexstyl.specialdates.events.peopleevents.CompositePeopleEventsProvider;
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsUpdater;
import com.alexstyl.specialdates.events.peopleevents.UpcomingEventsViewRefresher;
import com.alexstyl.specialdates.facebook.FacebookUserSettings;
import com.alexstyl.specialdates.home.HomeNavigator;
import com.alexstyl.specialdates.permissions.MementoPermissions;

import dagger.Module;
import dagger.Provides;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Module
public class UpcomingEventsModule {

    @Provides
    UpcomingEventsProvider providesUpcomingEventsProviderWithAds(Context context,
                                                                 Strings strings,
                                                                 Colors colors,
                                                                 NamedayUserSettings namedayUserSettings,
                                                                 NamedayCalendarProvider namedayCalendarProvider,
                                                                 CompositePeopleEventsProvider eventsProvider) {
        Date date = Date.Companion.today();
        return new CompositeUpcomingEventsProvider(
                eventsProvider,
                namedayUserSettings,
                namedayCalendarProvider,
                AndroidBankHolidaysPreferences.newInstance(context),
                new BankHolidayProvider(new GreekBankHolidaysCalculator(OrthodoxEasterCalculator.INSTANCE)),
                new UpcomingEventRowViewModelFactory(
                        date,
                        colors,
                        new AndroidUpcomingDateStringCreator(strings, date, context),
                        new ContactViewModelFactory(colors, strings)
                )
        );
    }

    @Provides
    HomeNavigator navigator(Analytics analytics, Strings strings, CrashAndErrorTracker tracker, FacebookUserSettings facebookUserSettings) {
        return new HomeNavigator(analytics, strings, facebookUserSettings, tracker);
    }

    @Provides
    UpcomingEventsJobCreator jobCreator(PeopleEventsUpdater updater, UpcomingEventsViewRefresher refresher) {
        return new UpcomingEventsJobCreator(updater);
    }

    @Provides
    UpcomingEventsPresenter presenter(MementoPermissions permissionsChecker, UpcomingEventsProvider provider) {
        Date today = Date.Companion.today();
        return new UpcomingEventsPresenter(
                today,
                permissionsChecker,
                provider,
                Schedulers.io(),
                AndroidSchedulers.mainThread()
        );
    }
}
