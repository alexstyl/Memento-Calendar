package com.alexstyl.specialdates.search

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView

class SuggstedNameViewHolder(itemView: View,
                             private val nameView: TextView) : RecyclerView.ViewHolder(itemView) {

    fun bind(name: String, onNamePressed: (String) -> Unit) {
        nameView.text = name
        nameView.setOnClickListener { onNamePressed(name) }

    }
}
