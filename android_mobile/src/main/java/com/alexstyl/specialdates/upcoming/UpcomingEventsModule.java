package com.alexstyl.specialdates.upcoming;

import android.content.Context;

import com.alexstyl.resources.Colors;
import com.alexstyl.specialdates.Strings;
import com.alexstyl.specialdates.analytics.Analytics;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.donate.DonationPreferences;
import com.alexstyl.specialdates.events.bankholidays.BankHolidayProvider;
import com.alexstyl.specialdates.events.bankholidays.BankHolidaysPreferences;
import com.alexstyl.specialdates.events.bankholidays.GreekBankHolidaysCalculator;
import com.alexstyl.specialdates.events.namedays.NamedayUserSettings;
import com.alexstyl.specialdates.events.namedays.calendar.OrthodoxEasterCalculator;
import com.alexstyl.specialdates.events.namedays.calendar.resource.NamedayCalendarProvider;
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsProvider;
import com.alexstyl.specialdates.facebook.FacebookPreferences;
import com.alexstyl.specialdates.home.HomeNavigator;
import com.alexstyl.specialdates.upcoming.widget.list.NoAds;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class UpcomingEventsModule {

    @Provides
    UpcomingEventsProvider providesUpcomingEventsProviderWithAds(Context context,
                                                                 Strings strings,
                                                                 Colors colors,
                                                                 NamedayUserSettings namedayUserSettings,
                                                                 NamedayCalendarProvider namedayCalendarProvider,
                                                                 PeopleEventsProvider eventsProvider) {
        Date date = Date.Companion.today();

        UpcomingEventsAdRules adRules = DonationPreferences.newInstance(context).hasDonated() ? new NoAds() : new UpcomingEventsFreeUserAdRules();
        return new UpcomingEventsProvider(eventsProvider,
                namedayUserSettings,
                namedayCalendarProvider,
                BankHolidaysPreferences.newInstance(context),
                new BankHolidayProvider(new GreekBankHolidaysCalculator(OrthodoxEasterCalculator.INSTANCE)),
                new UpcomingEventRowViewModelFactory(
                        date,
                        colors,
                        new UpcomingDateStringCreator(strings, date, context),
                        new ContactViewModelFactory(colors, strings)
                ), adRules
        );
    }

    @Provides
    @Named("widget")
    UpcomingEventsProvider providesUpcomingEventsProviderNoAds(Context context,
                                                               Strings strings,
                                                               Colors colors,
                                                               NamedayUserSettings namedayUserSettings,
                                                               NamedayCalendarProvider namedayCalendarProvider,
                                                               PeopleEventsProvider eventsProvider) {
        Date date = Date.Companion.today();
        UpcomingEventsAdRules adRules = new NoAds();
        return new UpcomingEventsProvider(eventsProvider,
                namedayUserSettings,
                namedayCalendarProvider,
                BankHolidaysPreferences.newInstance(context),
                new BankHolidayProvider(new GreekBankHolidaysCalculator(OrthodoxEasterCalculator.INSTANCE)),
                new UpcomingEventRowViewModelFactory(
                        date,
                        colors,
                        new UpcomingDateStringCreator(strings, date, context),
                        new ContactViewModelFactory(colors, strings)
                ), adRules
        );
    }

    @Provides
    HomeNavigator navigator(Analytics analytics, Strings strings, Context context) {
        return new HomeNavigator(analytics, strings, FacebookPreferences.newInstance(context));
    }
}
