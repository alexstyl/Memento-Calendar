package com.alexstyl.specialdates.upcoming.widget.list

import android.content.Context
import android.widget.RemoteViews

import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.upcoming.BankHolidayViewModel

internal class BankHolidayBinder(private val views: RemoteViews, private val context: Context) : UpcomingEventViewBinder<BankHolidayViewModel> {

    override fun bind(viewModel: BankHolidayViewModel) {
        views.setTextViewText(R.id.row_upcoming_bankholiday, viewModel.bankHolidayName)

        val fillInIntent = WidgetRouterActivity.buildIntent(context)
        views.setOnClickFillInIntent(R.id.widget_row_upcoming_bankholiday_background, fillInIntent)
    }

    override fun getViews(): RemoteViews = views
}
