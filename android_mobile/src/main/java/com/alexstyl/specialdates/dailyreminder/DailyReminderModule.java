package com.alexstyl.specialdates.dailyreminder;

import android.app.NotificationManager;
import android.content.Context;

import com.alexstyl.Logger;
import com.alexstyl.resources.Colors;
import com.alexstyl.specialdates.CrashAndErrorTracker;
import com.alexstyl.specialdates.EasyPreferences;
import com.alexstyl.specialdates.Strings;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.bankholidays.BankHolidayProvider;
import com.alexstyl.specialdates.events.bankholidays.BankHolidaysUserSettings;
import com.alexstyl.specialdates.events.namedays.NamedayUserSettings;
import com.alexstyl.specialdates.events.namedays.calendar.resource.NamedayCalendarProvider;
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsProvider;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.permissions.MementoPermissions;
import com.alexstyl.specialdates.settings.DailyReminderNavigator;

import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;

@Module
public class DailyReminderModule {

    @Provides
    DailyReminderUserSettings settings(Context context) {
        EasyPreferences defaultPreferences = EasyPreferences.createForDefaultPreferences(context);
        return new DailyReminderPreferences(defaultPreferences);
    }

    @Provides
    DailyReminderViewModelFactory factory(Strings strings, Colors colors) {
        return new AndroidDailyReminderViewModelFactory(strings, Date.Companion.today(), colors);
    }

    @Provides
    DailyReminderPresenter presenter(MementoPermissions permissions,
                                     PeopleEventsProvider peopleEventsProvider,
                                     NamedayUserSettings namedaySettings,
                                     BankHolidaysUserSettings bankholidaySettings,
                                     NamedayCalendarProvider namedayCalendarProvider,
                                     DailyReminderViewModelFactory factory,
                                     CrashAndErrorTracker errorTracker,
                                     BankHolidayProvider bankHolidayProvider) {
        return new DailyReminderPresenter(
                permissions,
                peopleEventsProvider,
                namedaySettings,
                bankholidaySettings,
                namedayCalendarProvider,
                factory,
                errorTracker,
                bankHolidayProvider,
                Schedulers.trampoline(),
                Schedulers.trampoline()
        );
    }

    @Provides
    DailyReminderNotifier notifier(Context context,
                                   ImageLoader imageLoader,
                                   Colors colors,
                                   NotificationManager notificationManager) {
        return new AndroidDailyReminderNotifier(context, notificationManager, imageLoader, colors);
    }

    @Provides
    DailyReminderOreoChannelCreator channelCreator(NotificationManager notificationManager,
                                                   Strings strings,
                                                   Logger logger,
                                                   DailyReminderUserSettings preferences) {
        return new DailyReminderOreoChannelCreator(notificationManager, strings, preferences, logger);
    }

    @Provides
    DailyReminderNavigator navigator() {
        return new DailyReminderNavigator();
    }

    @Provides
    DailyReminderScheduler scheduler() {
        return new AndroidDailyReminderScheduler();
    }
}
