package com.alexstyl.specialdates.people

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView

internal class ImportFromFacebookViewHolder(rowView: View, private val label: TextView) : RecyclerView.ViewHolder(rowView) {

    fun bind(viewModel: FacebookImportViewModel, listener: PeopleViewHolderListener) {
        label.text = viewModel.label
        itemView.setOnClickListener { listener.onFacebookImport() }
    }
}
