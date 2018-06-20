package com.alexstyl.specialdates.upcoming.widget.today

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import com.alexstyl.specialdates.MementoApplication
import com.alexstyl.specialdates.Strings
import com.alexstyl.specialdates.analytics.Analytics
import com.alexstyl.specialdates.analytics.Widget
import com.alexstyl.specialdates.date.DateLabelCreator
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

        val view = AndroidRecentPeopleEventsView(context,
                appWidgetManager,
                appWidgetIds,
                strings,
                preferences,
                widgetImageLoader,
                labelCreator
        )
        presenter.startPresentingInto(view)
        presenter.stopPresenting()
    }

}
