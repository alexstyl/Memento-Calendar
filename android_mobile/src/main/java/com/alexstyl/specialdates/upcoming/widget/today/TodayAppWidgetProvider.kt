package com.alexstyl.specialdates.upcoming.widget.today

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews

import com.alexstyl.specialdates.AppComponent
import com.alexstyl.specialdates.CrashAndErrorTracker
import com.alexstyl.specialdates.MementoApplication
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.Strings
import com.alexstyl.specialdates.analytics.Analytics
import com.alexstyl.specialdates.analytics.Widget
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.contact.ContactsProvider
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.DateLabelCreator
import com.alexstyl.specialdates.events.namedays.NamedayUserSettings
import com.alexstyl.specialdates.events.peopleevents.ContactEventsOnADate
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsProvider
import com.alexstyl.specialdates.home.HomeActivity
import com.alexstyl.specialdates.images.ImageLoader
import com.alexstyl.specialdates.permissions.MementoPermissions
import com.alexstyl.specialdates.person.PersonActivity
import com.alexstyl.specialdates.util.NaturalLanguageUtils

import javax.inject.Inject

class TodayAppWidgetProvider : AppWidgetProvider() {

    lateinit var preferences: UpcomingWidgetPreferences
        @Inject set
    lateinit var widgetImageLoader: WidgetImageLoader
        @Inject set
    lateinit var strings: Strings
        @Inject set
    lateinit var labelCreator: DateLabelCreator
        @Inject set
    lateinit var permissionChecker: MementoPermissions
        @Inject set
    lateinit var analytics: Analytics
        @Inject set
    lateinit var presenter: RecentPeopleEventsPresenter
        @Inject set

    override fun onReceive(context: Context, intent: Intent) {
        val applicationModule = (context.applicationContext as MementoApplication).applicationModule
        applicationModule.inject(this)
        super.onReceive(context, intent)
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        analytics.trackWidgetAdded(Widget.UPCOMING_EVENTS_SIMPLE)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        analytics.trackWidgetRemoved(Widget.UPCOMING_EVENTS_SIMPLE)
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        if (permissionChecker.canReadAndWriteContacts()) {
            val view = AndroidRecentPeopleEventsView(context, appWidgetManager, appWidgetIds, strings, preferences, widgetImageLoader, labelCreator)
            presenter.startPresentingInto(view)
        } else {
            // TODO asking for permission needs to go into the Presenter so that the WidgetProvider is responsible for triggering the updates of the events
            promptForContactPermission(context, appWidgetManager, appWidgetIds)
        }
    }

    private fun promptForContactPermission(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        val remoteViews = RemoteViews(context.packageName, R.layout.widget_prompt_permissions)
        remoteViews.setOnClickPendingIntent(R.id.widget_prompt_permission_background, pendingIntentToMain(context))
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews)
    }

    private fun pendingIntentToMain(context: Context): PendingIntent {
        val clickIntent = Intent(context, HomeActivity::class.java)
        return PendingIntent.getActivity(context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT)
    }
}
