package com.alexstyl.specialdates.upcoming.widget.list

import android.widget.RemoteViews

import com.alexstyl.specialdates.upcoming.UpcomingRowViewModel

interface UpcomingEventViewBinder {

    val views: RemoteViews

    fun bind(viewModel: UpcomingRowViewModel)
}
