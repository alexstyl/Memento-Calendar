package com.alexstyl.specialdates.search

import android.support.v7.util.DiffUtil
import java.lang.IllegalStateException

class NameSuggestionDiffCallback(private val oldResults: List<String>,
                                 private val newResults: List<String>) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return areContentsTheSame(oldItemPosition,newItemPosition)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldResults[oldItemPosition] == newResults[newItemPosition]
    }

    override fun getOldListSize() = oldResults.size

    override fun getNewListSize() = newResults.size

}
