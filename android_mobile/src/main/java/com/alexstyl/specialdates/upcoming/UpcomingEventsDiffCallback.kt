package com.alexstyl.specialdates.upcoming

import android.support.v7.util.DiffUtil

class UpcomingEventsDiffCallback(private val oldModels: List<UpcomingRowViewModel>,
                                 private val newModels: List<UpcomingRowViewModel>)
    : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldModels.size
    }

    override fun getNewListSize(): Int {
        return newModels.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldModels[oldItemPosition].viewType == newModels[newItemPosition].viewType
                && oldModels[oldItemPosition].id == newModels[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldModels[oldItemPosition] == newModels[newItemPosition]
    }
}
