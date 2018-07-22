package com.alexstyl.specialdates.debug

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView

class DebugOptionsAdapter(private val options: List<DebugOption>, private val listener: (DebugOption) -> Unit)
    : RecyclerView.Adapter<DebugOptionViewHolder>() {

    override fun getItemCount() = options.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DebugOptionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        val titleView = view.findViewById<TextView>(android.R.id.text1)
        return DebugOptionViewHolder(view, titleView)
    }

    override fun onBindViewHolder(holder: DebugOptionViewHolder, position: Int) {
        holder.bind(options[position], listener)
    }
}