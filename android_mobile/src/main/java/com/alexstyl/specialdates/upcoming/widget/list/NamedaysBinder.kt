package com.alexstyl.specialdates.upcoming.widget.list

import android.content.Context
import android.widget.RemoteViews
import com.alexstyl.specialdates.R

import com.alexstyl.specialdates.upcoming.NamedaysViewModel

internal class NamedaysBinder(private val views: RemoteViews, private val context: Context) : UpcomingEventViewBinder<NamedaysViewModel> {

    override fun bind(viewModel: NamedaysViewModel) {
        views.setTextViewText(R.id.row_upcoming_namedays, viewModel.namesLabel)

        views.setOnClickFillInIntent(R.id.widget_row_upcoming_nameday_background, WidgetRouterActivity.buildNamedayIntent(viewModel.date, context))
    }

    override fun getViews(): RemoteViews = views
}
