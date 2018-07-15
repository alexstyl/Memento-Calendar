package com.alexstyl.specialdates.upcoming.widget.list

import android.widget.RemoteViews

import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.upcoming.DateHeaderViewModel
import com.alexstyl.specialdates.upcoming.UpcomingRowViewModel

class DateHeaderBinder(private val _views: RemoteViews) : UpcomingEventViewBinder {
    override fun bind(viewModel: UpcomingRowViewModel) {
        viewModel as DateHeaderViewModel
        views.setTextViewText(R.id.upcoming_event_date_header, viewModel.date)
    }

    override val views: RemoteViews
        get() = _views
}
