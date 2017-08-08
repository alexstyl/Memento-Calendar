package com.alexstyl.specialdates.upcoming

import android.view.View
import android.widget.TextView
import com.alexstyl.specialdates.upcoming.view.OnUpcomingEventClickedListener

class BankholidayViewHolder(view: View,
                            private val holidayName: TextView)
    : UpcomingRowViewHolder<BankHolidayViewModel>(view) {

    override fun bind(viewModel: BankHolidayViewModel, listener: OnUpcomingEventClickedListener) {
        holidayName.text = viewModel.bankHolidayName
    }

}
