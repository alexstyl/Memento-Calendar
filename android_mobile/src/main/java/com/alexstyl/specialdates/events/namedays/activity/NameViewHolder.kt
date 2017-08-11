package com.alexstyl.specialdates.events.namedays.activity

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView

class NameViewHolder(view: View, private val nameView: TextView) : RecyclerView.ViewHolder(view) {
    fun bind(namedaysViewModel: NamedaysViewModel) {
        nameView.text = namedaysViewModel.name
    }
}
