package com.alexstyl.specialdates.upcoming

import android.view.View
import android.widget.TextView

import com.alexstyl.specialdates.upcoming.view.OnUpcomingEventClickedListener

internal class DateHeaderViewHolder(view: View, private val dateView: TextView) : UpcomingRowViewHolder<DateHeaderViewModel>(view) {
    override fun bind(viewModel: DateHeaderViewModel, listener: OnUpcomingEventClickedListener) {
        dateView.text = viewModel.date
        dateView.setTextColor(viewModel.titleColor)
    }
}
