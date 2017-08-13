package com.alexstyl.specialdates.events.namedays.activity

import android.support.v7.widget.RecyclerView
import android.view.View
import com.alexstyl.specialdates.contact.Contact

abstract class NamedayScreenViewHolder<in T : NamedayScreenViewModel>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(viewModel: T, onContactClicked: (Contact) -> Unit)
}
