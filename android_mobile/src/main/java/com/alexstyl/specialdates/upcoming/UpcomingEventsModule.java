package com.alexstyl.specialdates.upcoming;

import android.content.Context;

import com.alexstyl.resources.ColorResources;
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

import javax.inject.Named;
import java.util.Locale;

import dagger.Module;
import dagger.Provides;

@Module
public class UpcomingEventsModule {

    private final Context context;

    public UpcomingEventsModule(Context context) {
        this.context = context;
    }

    @Provides
    UpcomingEventsProvider providesUpcomingEventsProviderWithAds(StringResources stringResources, ColorResources colorResources) {
        Date date = Date.Companion.today();

        UpcomingEventsAdRules adRules = DonationPreferences.newInstance(context).hasDonated() ? new NoAds() : new UpcomingEventsFreeUserAdRules();
        return new UpcomingEventsProvider(PeopleEventsProvider.newInstance(context),
                                          NamedayPreferences.newInstance(context),
                                          BankHolidaysPreferences.newInstance(context),
                                          new BankHolidayProvider(new GreekBankHolidaysCalculator(OrthodoxEasterCalculator.INSTANCE)),
                                          NamedayCalendarProvider.newInstance(context.getResources()),
                                          new UpcomingEventRowViewModelFactory(
                                                  date,
                                                  new UpcomingDateStringCreator(stringResources, date),
                                                  new ContactViewModelFactory(colorResources, stringResources),
                                                  stringResources,
                                                  new BankHolidayViewModelFactory(),
                                                  new NamedaysViewModelFactory(date),
                                                  MonthLabels.forLocale(Locale.getDefault())
                                          ), adRules
        );
    }

    @Provides
    @Named("widget")
    UpcomingEventsProvider providesUpcomingEventsProviderNoAds(StringResources stringResources, ColorResources colorResources) {
        Date date = Date.Companion.today();

        UpcomingEventsAdRules adRules = new NoAds();
        return new UpcomingEventsProvider(PeopleEventsProvider.newInstance(context),
                                          NamedayPreferences.newInstance(context),
                                          BankHolidaysPreferences.newInstance(context),
                                          new BankHolidayProvider(new GreekBankHolidaysCalculator(OrthodoxEasterCalculator.INSTANCE)),
                                          NamedayCalendarProvider.newInstance(context.getResources()),
                                          new UpcomingEventRowViewModelFactory(
                                                  date,
                                                  new UpcomingDateStringCreator(stringResources, date),
                                                  new ContactViewModelFactory(colorResources, stringResources),
                                                  stringResources,
                                                  new BankHolidayViewModelFactory(),
                                                  new NamedaysViewModelFactory(date),
                                                  MonthLabels.forLocale(Locale.getDefault())
                                          ), adRules
        );
    }
}
