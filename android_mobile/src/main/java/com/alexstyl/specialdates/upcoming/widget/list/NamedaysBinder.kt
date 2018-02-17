package com.alexstyl.specialdates.upcoming.widget.list

import android.content.Context
import android.widget.RemoteViews
import com.alexstyl.specialdates.R

import com.alexstyl.specialdates.upcoming.UpcomingNamedaysViewModel

internal class NamedaysBinder(private val views: RemoteViews, private val context: Context) : UpcomingEventViewBinder<UpcomingNamedaysViewModel> {

    override fun bind(viewModelUpcoming: UpcomingNamedaysViewModel) {
        views.setTextViewText(R.id.row_upcoming_namedays, viewModelUpcoming.namesLabel)

        views.setOnClickFillInIntent(R.id.widget_row_upcoming_nameday_background, WidgetRouterActivity.buildNamedayIntent(viewModelUpcoming.date, context))
    }

    override fun getViews(): RemoteViews = views
}
