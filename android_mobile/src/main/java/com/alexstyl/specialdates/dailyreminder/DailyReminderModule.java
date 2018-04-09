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
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Module
public class DailyReminderModule {

    @Provides
    DailyReminderUserSettings settings(Context context) {
        EasyPreferences defaultPreferences = EasyPreferences.createForDefaultPreferences(context);
        return new DailyReminderPreferences(defaultPreferences);
    }

    @Provides
    NotificationViewModelFactory factory(Strings strings, Colors colors) {
        return new AndroidNotificationViewModelFactory(strings, Date.Companion.today(), colors);
    }

    @Provides
    DailyReminderPresenter presenter(MementoPermissions permissions,
                                     PeopleEventsProvider peopleEventsProvider,
                                     NamedayUserSettings namedaySettings,
                                     BankHolidaysUserSettings bankholidaySettings,
                                     NamedayCalendarProvider namedayCalendarProvider,
                                     NotificationViewModelFactory factory,
                                     CrashAndErrorTracker errorTracker,
                                     BankHolidayProvider bankHolidayProvider) {
        return new DailyReminderPresenter(permissions,
                                          peopleEventsProvider,
                                          namedaySettings,
                                          bankholidaySettings,
                                          namedayCalendarProvider,
                                          factory,
                                          errorTracker,
                                          bankHolidayProvider,
                                          Schedulers.io(), AndroidSchedulers.mainThread()
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
    DailyReminderOreoChannelCreator channelCreator(NotificationManager notificatioManager, Strings strings, Logger logger) {
        return new DailyReminderOreoChannelCreator(notificatioManager, strings, logger);
    }

    @Provides
    DailyReminderNavigator navigator() {
        return new DailyReminderNavigator();
    }


    @Provides
    DailyReminderScheduler scheduler(AlarmManagerCompat alarmManager, Context context) {
        return new DailyReminderScheduler(alarmManager, context);
    }
}
