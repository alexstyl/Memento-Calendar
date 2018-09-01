package com.alexstyl.specialdates.dailyreminder

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context
import com.alexstyl.Logger
import com.alexstyl.resources.Colors
import com.alexstyl.specialdates.BuildConfig
import com.alexstyl.specialdates.CrashAndErrorTracker
import com.alexstyl.specialdates.EasyPreferences
import com.alexstyl.specialdates.Strings
import com.alexstyl.specialdates.analytics.Analytics
import com.alexstyl.specialdates.dailyreminder.log.DailyReminderLogger
import com.alexstyl.specialdates.date.DateLabelCreator
import com.alexstyl.specialdates.date.todaysDate
import com.alexstyl.specialdates.events.bankholidays.BankHolidayProvider
import com.alexstyl.specialdates.events.bankholidays.BankHolidaysUserSettings
import com.alexstyl.specialdates.events.namedays.NamedayUserSettings
import com.alexstyl.specialdates.events.namedays.calendar.resource.NamedayCalendarProvider
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsProvider
import com.alexstyl.specialdates.images.ImageLoader
import com.alexstyl.specialdates.permissions.MementoPermissions
import com.alexstyl.specialdates.settings.DailyReminderNavigator
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers

@Module
class DailyReminderModule {

    @Provides
    fun settings(context: Context): DailyReminderUserSettings {
        val defaultPreferences = EasyPreferences.createForDefaultPreferences(context)
        return DailyReminderPreferences(defaultPreferences)
    }

    @Provides
    fun factory(strings: Strings, colors: Colors): DailyReminderViewModelFactory {
        return AndroidDailyReminderViewModelFactory(strings, todaysDate(), colors)
    }

    @Provides
    fun presenter(permissions: MementoPermissions,
                  peopleEventsProvider: PeopleEventsProvider,
                  namedaySettings: NamedayUserSettings,
                  bankholidaySettings: BankHolidaysUserSettings,
                  namedayCalendarProvider: NamedayCalendarProvider,
                  factory: DailyReminderViewModelFactory,
                  errorTracker: CrashAndErrorTracker,
                  bankHolidayProvider: BankHolidayProvider,
                  analytics: Analytics,
                  logger: DailyReminderLogger): DailyReminderPresenter {
        return DailyReminderPresenter(
                permissions,
                peopleEventsProvider,
                namedaySettings,
                bankholidaySettings,
                namedayCalendarProvider,
                factory,
                errorTracker,
                bankHolidayProvider,
                analytics,
                Schedulers.trampoline(),
                Schedulers.trampoline(),
                logger
        )
    }

    @Provides
    fun logger(context: Context, dateLabel: DateLabelCreator): DailyReminderLogger {
        val repository = if (BuildConfig.DEBUG) {
            AndroidLoggedEventsRepository(context)
        } else {
            NoLoggedEvents()
        }
        return DailyReminderLogger(dateLabel, repository)
    }

    @Provides
    fun notifier(context: Context,
                 imageLoader: ImageLoader,
                 colors: Colors,
                 notificationManager: NotificationManager): DailyReminderNotifier {
        return AndroidDailyReminderNotifier(context, notificationManager, imageLoader, colors)
    }

    @Provides
    fun channelCreator(notificationManager: NotificationManager,
                       strings: Strings,
                       logger: Logger,
                       preferences: DailyReminderUserSettings): DailyReminderOreoChannelCreator {
        return DailyReminderOreoChannelCreator(notificationManager, strings, preferences, logger)
    }

    @Provides
    fun navigator(): DailyReminderNavigator {
        return DailyReminderNavigator()
    }

    @Provides
    fun scheduler(context: Context, alarmManager: AlarmManager): DailyReminderScheduler {
        return AndroidDailyReminderScheduler(context, alarmManager)
    }
}
