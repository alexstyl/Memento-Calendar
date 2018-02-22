package com.alexstyl.specialdates.upcoming

import android.support.v7.util.DiffUtil

internal class UpcomingDiffCallback(private val oldModels: List<UpcomingRowViewModel>,
                                    private val newModels: List<UpcomingRowViewModel>)
    : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldModels.size
    }

    override fun getNewListSize(): Int {
        return newModels.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return areOfSameType(oldItemPosition, newItemPosition)
                && oldModels[oldItemPosition].id == newModels[newItemPosition].id
    }

    private fun areOfSameType(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldModels[oldItemPosition].viewType == newModels[newItemPosition].viewType
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldModel = oldModels[oldItemPosition]
        val newModel = newModels[newItemPosition]
        return oldModel === newModel
    }
}
