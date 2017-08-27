package com.alexstyl.specialdates.upcoming;

import android.content.Context;

import com.alexstyl.resources.ColorResources;
import com.alexstyl.resources.StringResources;
import com.alexstyl.specialdates.contact.ContactsProvider;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.donate.DonationPreferences;
import com.alexstyl.specialdates.events.bankholidays.BankHolidayProvider;
import com.alexstyl.specialdates.events.bankholidays.BankHolidaysPreferences;
import com.alexstyl.specialdates.events.bankholidays.GreekBankHolidaysCalculator;
import com.alexstyl.specialdates.events.namedays.NamedayUserSettings;
import com.alexstyl.specialdates.events.namedays.calendar.OrthodoxEasterCalculator;
import com.alexstyl.specialdates.events.namedays.calendar.resource.NamedayCalendarProvider;
import com.alexstyl.specialdates.service.PeopleEventsProvider;
import com.alexstyl.specialdates.upcoming.widget.list.NoAds;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class UpcomingEventsModule {

    @Provides
    UpcomingEventsProvider providesUpcomingEventsProviderWithAds(Context context,
                                                                 StringResources stringResources,
                                                                 ColorResources colorResources,
                                                                 NamedayUserSettings namedayUserSettings,
                                                                 NamedayCalendarProvider namedayCalendarProvider,
                                                                 ContactsProvider provider) {
        Date date = Date.Companion.today();

        UpcomingEventsAdRules adRules = DonationPreferences.newInstance(context).hasDonated() ? new NoAds() : new UpcomingEventsFreeUserAdRules();
        return new UpcomingEventsProvider(PeopleEventsProvider.newInstance(context, namedayUserSettings, provider),
                                          namedayUserSettings,
                                          namedayCalendarProvider,
                                          BankHolidaysPreferences.newInstance(context),
                                          new BankHolidayProvider(new GreekBankHolidaysCalculator(OrthodoxEasterCalculator.INSTANCE)),
                                          new UpcomingEventRowViewModelFactory(
                                                  date,
                                                  new UpcomingDateStringCreator(stringResources, date, context),
                                                  new ContactViewModelFactory(colorResources, stringResources)
                                          ), adRules
        );
    }

    @Provides
    @Named("widget")
    UpcomingEventsProvider providesUpcomingEventsProviderNoAds(Context context,
                                                               StringResources stringResources,
                                                               ColorResources colorResources,
                                                               NamedayUserSettings namedayUserSettings,
                                                               NamedayCalendarProvider namedayCalendarProvider,
                                                               ContactsProvider provider) {
        Date date = Date.Companion.today();
        UpcomingEventsAdRules adRules = new NoAds();
        return new UpcomingEventsProvider(PeopleEventsProvider.newInstance(context, namedayUserSettings, provider),
                                          namedayUserSettings,
                                          namedayCalendarProvider,
                                          BankHolidaysPreferences.newInstance(context),
                                          new BankHolidayProvider(new GreekBankHolidaysCalculator(OrthodoxEasterCalculator.INSTANCE)),
                                          new UpcomingEventRowViewModelFactory(
                                                  date,
                                                  new UpcomingDateStringCreator(stringResources, date, context),
                                                  new ContactViewModelFactory(colorResources, stringResources)
                                          ), adRules
        );
    }
}
