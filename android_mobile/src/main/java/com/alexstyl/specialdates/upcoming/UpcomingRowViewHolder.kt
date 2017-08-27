package com.alexstyl.specialdates.upcoming

import android.support.v7.widget.RecyclerView
import android.view.View

import com.alexstyl.specialdates.upcoming.view.OnUpcomingEventClickedListener

abstract class UpcomingRowViewHolder<in T : UpcomingRowViewModel>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun bind(viewModel: T, listener: OnUpcomingEventClickedListener)
}
