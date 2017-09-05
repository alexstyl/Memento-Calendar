package com.alexstyl.specialdates.upcoming.widget.list

import android.widget.RemoteViews

import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.upcoming.DateHeaderViewModel

class DateHeaderBinder(private val views: RemoteViews) : UpcomingEventViewBinder<DateHeaderViewModel> {

    override fun bind(viewModel: DateHeaderViewModel) {
        views.setTextViewText(R.id.upcoming_event_date_header, viewModel.date)
    }

    override fun getViews(): RemoteViews = views
}
