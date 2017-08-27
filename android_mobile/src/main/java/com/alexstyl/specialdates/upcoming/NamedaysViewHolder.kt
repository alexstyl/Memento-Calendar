package com.alexstyl.specialdates.upcoming

import android.view.View
import android.widget.TextView
import com.alexstyl.specialdates.upcoming.view.OnUpcomingEventClickedListener

class NamedaysViewHolder(view: View, private val namedays: TextView)
    : UpcomingRowViewHolder<NamedaysViewModel>(view) {


    override fun bind(viewModel: NamedaysViewModel, listener: OnUpcomingEventClickedListener) {
        namedays.text = viewModel.namesLabel
        itemView.setOnClickListener { listener.onNamedayClicked(viewModel.date) }
    }


}
