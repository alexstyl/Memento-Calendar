package com.alexstyl.specialdates

import android.app.NotificationManager
import android.appwidget.AppWidgetManager
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import com.alexstyl.Logger
import com.alexstyl.android.AndroidLogger
import com.alexstyl.specialdates.dailyreminder.DailyReminderNotifier
import com.alexstyl.specialdates.dailyreminder.DailyReminderPresenter
import com.alexstyl.specialdates.events.database.EventSQLiteOpenHelper
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsUpdater
import com.alexstyl.specialdates.facebook.friendimport.FacebookFriendsUpdater
import com.alexstyl.specialdates.permissions.AndroidPermissions
import com.alexstyl.specialdates.permissions.MementoPermissions
import com.evernote.android.job.JobManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal class AndroidApplicationModule(private val context: Context) {

    @Provides
    fun appContext(): Context {
        return context
    }

    @Provides
    fun resources(): Resources {
        return context.resources
    }

    @Provides
    fun pkgManager(): PackageManager {
        return context.packageManager
    }

    @Provides
    fun contentResolver(): ContentResolver {
        return context.contentResolver
    }

    @Provides
    fun appWidgetManager(): AppWidgetManager {
        return AppWidgetManager.getInstance(context)
    }

    @Provides
    @Singleton
    fun sqLiteOpenHelper(): EventSQLiteOpenHelper {
        return EventSQLiteOpenHelper(context)
    }

    @Provides
    fun permissionChecker(tracker: CrashAndErrorTracker): MementoPermissions {
        return AndroidPermissions(tracker, context)
    }

    @Provides
    fun tracker(): CrashAndErrorTracker {
        return FabricTracker(context)
    }

    @Provides
    @Singleton
    fun jobManager(): JobManager {
        return JobManager.create(context)
    }

    @Provides
    fun notificationManager(): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    @Provides
    fun logger(): Logger {
        return AndroidLogger()
    }

    @Provides
    fun jobCreator(updater: PeopleEventsUpdater,
                   presenter: DailyReminderPresenter,
                   notifier: DailyReminderNotifier,
                   permissions: MementoPermissions,
                   facebookFriendsUpdater: FacebookFriendsUpdater,
                   tracker: CrashAndErrorTracker): JobsCreator {
        return JobsCreator(updater, presenter, notifier, permissions, facebookFriendsUpdater, tracker)
    }

    @Provides
    fun mementoSettings(context: Context): MementoUserSettings {
        return AndroidMementoUserSettings.create(context)
    }
}
