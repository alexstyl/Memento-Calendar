package com.alexstyl.specialdates.upcoming.widget.list

import android.widget.RemoteViews

import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.upcoming.BankHolidayViewModel

internal class BankHolidayBinder(private val views: RemoteViews) : UpcomingEventViewBinder<BankHolidayViewModel> {

    override fun bind(viewModel: BankHolidayViewModel) {
        views.setTextViewText(R.id.row_upcoming_bankholiday, viewModel.bankHolidayName)
    }

    override fun getViews(): RemoteViews = views
}
