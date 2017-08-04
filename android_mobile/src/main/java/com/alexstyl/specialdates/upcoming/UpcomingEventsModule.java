package com.alexstyl.specialdates.upcoming;

import android.content.Context;

import com.alexstyl.resources.ColorResources;
import com.alexstyl.resources.StringResources;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.bankholidays.BankHolidayProvider;
import com.alexstyl.specialdates.events.bankholidays.BankHolidaysPreferences;
import com.alexstyl.specialdates.events.bankholidays.GreekBankHolidaysCalculator;
import com.alexstyl.specialdates.events.namedays.NamedayPreferences;
import com.alexstyl.specialdates.events.namedays.calendar.OrthodoxEasterCalculator;
import com.alexstyl.specialdates.events.namedays.calendar.resource.NamedayCalendarProvider;
import com.alexstyl.specialdates.service.PeopleEventsProvider;
import com.alexstyl.specialdates.upcoming.widget.list.NoAds;
import com.alexstyl.specialdates.upcoming.widget.list.UpcomingEventsProvider;

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
    UpcomingEventsProvider providesUpcomingEventsProvider(StringResources stringResources, ColorResources colorResources) {
        Date date = Date.Companion.today();

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
                                          )
                , new NoAds()
        );
    }
}
