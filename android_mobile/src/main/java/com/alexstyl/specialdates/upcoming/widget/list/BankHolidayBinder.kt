package com.alexstyl.specialdates.upcoming.widget.list

import android.content.Context
import android.widget.RemoteViews

import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.upcoming.BankHolidayViewModel
import com.alexstyl.specialdates.upcoming.UpcomingRowViewModel

class BankHolidayBinder(private val _views: RemoteViews, private val context: Context) : UpcomingEventViewBinder {
    override fun bind(viewModel: UpcomingRowViewModel) {
        viewModel as BankHolidayViewModel
        views.setTextViewText(R.id.row_upcoming_bankholiday, viewModel.bankHolidayName)

        val fillInIntent = WidgetRouterActivity.buildIntent(context)
        views.setOnClickFillInIntent(R.id.widget_row_upcoming_bankholiday_background, fillInIntent)
    }

    override val views: RemoteViews
        get() = _views

}
