package com.alexstyl.specialdates.upcoming.widget.today

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.Strings
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.DateLabelCreator
import com.alexstyl.specialdates.events.peopleevents.ContactEventsOnADate
import com.alexstyl.specialdates.home.HomeActivity
import com.alexstyl.specialdates.person.PersonActivity
import com.alexstyl.specialdates.util.NaturalLanguageUtils

class AndroidRecentPeopleEventsView(private val context: Context,
                                    private val appWidgetManager: AppWidgetManager,
                                    private val appWidgetIds: IntArray,
                                    private val strings: Strings,
                                    private val preferences: UpcomingWidgetPreferences,
                                    private val widgetImageLoader: WidgetImageLoader,
                                    private val labelCreator: DateLabelCreator)
    : RecentPeopleEventsView {

    override fun onNoEventsFound() {
        appWidgetIds.forEach { appWidgetId ->

            val views = RemoteViews(context.packageName, R.layout.widget_today_nocontacts)
            val intent = Intent(context, HomeActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                    context, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            )

            views.setTextViewText(android.R.id.text1, context.getString(R.string.today_widget_empty))
            views.setOnClickPendingIntent(R.id.upcoming_widget_background, pendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onNextDateLoaded(events: ContactEventsOnADate) {
        val eventDate = events.date
        val date = Date.on(eventDate.dayOfMonth, eventDate.month, Date.today().year)
        val intent = HomeActivity.getStartIntent(context)
        intent.data = Uri.parse(date.hashCode().toString())

        val contacts = events.contacts
        val pendingIntent = pendingIntentFor(context, intent, contacts)
        val title = labelOf(date)

        val label = NaturalLanguageUtils.joinContacts(strings, events.contacts, 2)

        val selectedVariant = preferences.selectedVariant
        val transparencyColorCalculator = TransparencyColorCalculator()
        val opacity = preferences.oppacityLevel
        val selectedTextColor = context.resources.getColor(selectedVariant.textColor)

        val calculator = WidgetColorCalculator(selectedTextColor)
        val finalHeaderColor = calculator.getColor(Date.today(), date)
        val avatarSizeInPx = context.resources.getDimensionPixelSize(R.dimen.widget_avatar_size)
        for (appWidgetId in appWidgetIds) {
            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            val remoteViews = RemoteViews(context.packageName, R.layout.widget_today)

            remoteViews.setTextViewText(R.id.upcoming_widget_header, title)
            remoteViews.setTextViewText(R.id.upcoming_widget_events_text, label)

            remoteViews.setTextColor(R.id.upcoming_widget_events_text, selectedTextColor)
            remoteViews.setTextColor(R.id.upcoming_widget_header, finalHeaderColor)

            val background = context.resources.getColor(selectedVariant.backgroundColorResId)
            val backgroundColor = transparencyColorCalculator.calculateColor(background, opacity)
            remoteViews.setInt(R.id.upcoming_widget_background_image, "setBackgroundColor", backgroundColor)

            remoteViews.setOnClickPendingIntent(R.id.upcoming_widget_background, pendingIntent)
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews)

            widgetImageLoader.loadPicture(events.contacts, appWidgetId, remoteViews, avatarSizeInPx)
        }
    }

    private fun pendingIntentFor(context: Context, intent: Intent, contacts: List<Contact>): PendingIntent {
        val pendingIntent: PendingIntent
        if (contacts.size == 1) {
            pendingIntent = PendingIntent.getActivity(context, 0, PersonActivity.buildIntentFor(context,
                    contacts[0]), PendingIntent.FLAG_UPDATE_CURRENT)
        } else {
            pendingIntent = PendingIntent.getActivity(
                    context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        return pendingIntent
    }

    private fun labelOf(todayDate: Date): String {
        return labelCreator.createWithYearPreferred(todayDate)
    }


}
