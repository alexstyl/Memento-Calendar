package com.alexstyl.specialdates.debug

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView

class DebugOptionViewHolder(private val view: View, private val titleView: TextView) : RecyclerView.ViewHolder(view) {
    fun bind(option: DebugOption, listener: (DebugOption) -> Unit) {
        titleView.text = option.title
        view.setOnClickListener { listener(option) }
    }
}