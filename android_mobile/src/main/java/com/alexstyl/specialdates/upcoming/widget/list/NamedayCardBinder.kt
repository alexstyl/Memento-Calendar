package com.alexstyl.specialdates.upcoming.widget.list

import android.widget.RemoteViews
import com.alexstyl.specialdates.R

import com.alexstyl.specialdates.upcoming.NamedaysViewModel

internal class NamedayCardBinder(private val views: RemoteViews) : UpcomingEventViewBinder<NamedaysViewModel> {

    override fun bind(viewModel: NamedaysViewModel) {
        views.setTextViewText(R.id.row_upcoming_namedays, viewModel.namesLabel)
    }

    override fun getViews(): RemoteViews = views
}
