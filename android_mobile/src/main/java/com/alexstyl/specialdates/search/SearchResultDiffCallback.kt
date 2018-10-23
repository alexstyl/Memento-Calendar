package com.alexstyl.specialdates.search

import android.support.v7.util.DiffUtil
import java.lang.IllegalStateException

class SearchResultDiffCallback(private val oldResults: List<SearchResultViewModel>,
                               private val newResults: List<SearchResultViewModel>) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {

        if (oldResults[oldItemPosition]::class.java != newResults[newItemPosition]::class.java) {
            return false
        }
        val old = oldResults[oldItemPosition]
        if (old is ContactSearchResultViewModel) {
            val new = newResults[newItemPosition] as ContactSearchResultViewModel
            return old.contact.contactID == new.contact.contactID
        }

        if (old is NamedaySearchResultViewModel) {
            val new = newResults[newItemPosition] as NamedaySearchResultViewModel
            return old.name == new.name
        }

        if (old is ContactReadPermissionRequestViewModel) {
            return newResults[newItemPosition] is ContactReadPermissionRequestViewModel
        }
        throw IllegalStateException("Cannot compare types of ${old::class.java}")
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldResults[oldItemPosition] == newResults[newItemPosition]
    }

    override fun getOldListSize() = oldResults.size

    override fun getNewListSize() = newResults.size

}
