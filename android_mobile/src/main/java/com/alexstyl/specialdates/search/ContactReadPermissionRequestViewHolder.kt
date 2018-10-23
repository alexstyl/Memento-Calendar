package com.alexstyl.specialdates.search

import android.support.v7.widget.RecyclerView
import android.view.View

class ContactReadPermissionRequestViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(listener: SearchResultClickListener) {
        this.itemView.setOnClickListener { listener.onContactReadPermissionClicked() }
    }
}
