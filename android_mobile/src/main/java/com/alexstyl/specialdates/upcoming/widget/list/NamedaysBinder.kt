package com.alexstyl.specialdates.upcoming.widget.list

import android.content.Context
import android.widget.RemoteViews
import com.alexstyl.specialdates.R

import com.alexstyl.specialdates.upcoming.UpcomingNamedaysViewModel
import com.alexstyl.specialdates.upcoming.UpcomingRowViewModel

internal class NamedaysBinder(private val _views: RemoteViews, private val context: Context) : UpcomingEventViewBinder {

    override fun bind(viewModelUpcoming: UpcomingRowViewModel) {
        viewModelUpcoming as UpcomingNamedaysViewModel
        views.setTextViewText(R.id.row_upcoming_namedays, viewModelUpcoming.namesLabel)

        views.setOnClickFillInIntent(R.id.widget_row_upcoming_nameday_background,
                WidgetRouterActivity.buildNamedayIntent(viewModelUpcoming.date, context)
        )
    }

    override val views: RemoteViews
        get() = _views
}
