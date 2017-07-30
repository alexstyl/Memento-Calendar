package com.alexstyl.specialdates.upcoming.widget.list;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.alexstyl.resources.ColorResources;
import com.alexstyl.resources.DimensionResources;
import com.alexstyl.resources.StringResources;
import com.alexstyl.specialdates.AppComponent;
import com.alexstyl.specialdates.MementoApplication;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.bankholidays.BankHolidayProvider;
import com.alexstyl.specialdates.events.bankholidays.BankHolidaysPreferences;
import com.alexstyl.specialdates.events.bankholidays.GreekBankHolidaysCalculator;
import com.alexstyl.specialdates.events.namedays.NamedayPreferences;
import com.alexstyl.specialdates.events.namedays.calendar.OrthodoxEasterCalculator;
import com.alexstyl.specialdates.events.namedays.calendar.resource.NamedayCalendarProvider;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.service.PeopleEventsProvider;
import com.alexstyl.specialdates.upcoming.BankHolidayViewModelFactory;
import com.alexstyl.specialdates.upcoming.ContactViewModelFactory;
import com.alexstyl.specialdates.upcoming.MonthLabels;
import com.alexstyl.specialdates.upcoming.NamedaysViewModelFactory;
import com.alexstyl.specialdates.upcoming.UpcomingDateStringCreator;
import com.alexstyl.specialdates.upcoming.UpcomingEventRowViewModelFactory;

import javax.inject.Inject;
import java.util.Locale;

public class UpcomingEventsRemoteViewService extends RemoteViewsService {

    @Inject StringResources stringResources;
    @Inject DimensionResources dimensResources;
    @Inject ColorResources colorResources;
    @Inject ImageLoader imageLoader;

    @Override
    public void onCreate() {
        super.onCreate();
        AppComponent applicationModule = ((MementoApplication) getApplication()).getApplicationModule();
        applicationModule.inject(this);
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        UpcomingEventsProvider peopleEventsProvider = createPeopleEventsProvider();
        CircularAvatarFactory avatarFactory = new CircularAvatarFactory(
                imageLoader,
                colorResources
        );
        return new UpcomingEventsViewsFactory(
                getPackageName(),
                peopleEventsProvider,
                dimensResources,
                this,
                avatarFactory
        );
    }

    private UpcomingEventsProvider createPeopleEventsProvider() {
        Date today = Date.today();
        return new UpcomingEventsProvider(
                PeopleEventsProvider.newInstance(this),
                NamedayPreferences.newInstance(this),
                BankHolidaysPreferences.newInstance(this),
                new BankHolidayProvider(new GreekBankHolidaysCalculator(OrthodoxEasterCalculator.INSTANCE)),
                NamedayCalendarProvider.newInstance(getResources()),
                new UpcomingEventRowViewModelFactory(
                        today,
                        new UpcomingDateStringCreator(stringResources, today),
                        new ContactViewModelFactory(colorResources, stringResources),
                        stringResources,
                        new BankHolidayViewModelFactory(),
                        new NamedaysViewModelFactory(today),
                        MonthLabels.forLocale(Locale.getDefault())
                ),
                new NoAds()
        );
    }

}
