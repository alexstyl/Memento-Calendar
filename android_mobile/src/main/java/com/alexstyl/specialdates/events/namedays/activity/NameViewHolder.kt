package com.alexstyl.specialdates.events.namedays.activity

import android.view.View
import android.widget.TextView
import com.alexstyl.specialdates.contact.Contact

class NameViewHolder(view: View,
                     private val nameView: TextView)
    : NamedayScreenViewHolder<NamedaysViewModel>(view) {

    override fun bind(viewModel: NamedaysViewModel, onContactClicked: (Contact) -> Unit) {
        nameView.text = viewModel.name
    }
}
