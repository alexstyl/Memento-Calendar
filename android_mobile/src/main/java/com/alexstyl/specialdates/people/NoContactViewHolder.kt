package com.alexstyl.specialdates.people

import android.support.v7.widget.RecyclerView
import android.view.View

class NoContactViewHolder(rowView: View) : RecyclerView.ViewHolder(rowView) {

    fun bind(listener: PeopleViewHolderListener) {
        itemView.setOnClickListener { listener.onAddContactClicked() }
    }
}
