package com.alexstyl.specialdates.people

import android.support.v7.util.DiffUtil

class PeopleDiffCallback(private val oldViewModels: List<PeopleRowViewModel>,
                         private val newViewModels: List<PeopleRowViewModel>)
    : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldViewModels.size
    }

    override fun getNewListSize(): Int {
        return newViewModels.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldViewModel = oldViewModels[oldItemPosition]
        val newViewModel = newViewModels[newItemPosition]

        if (oldViewModel is FacebookImportViewModel && newViewModel is FacebookImportViewModel) {
            return true
        }

        if (oldViewModel is NoContactsViewModel && newViewModel is NoContactsViewModel) {
            return true
        }

        if (oldViewModel is PersonViewModel && newViewModel is PersonViewModel) {
            val (_, _, _, oldModelId, oldModelSource) = oldViewModels[oldItemPosition] as PersonViewModel
            val (_, _, _, newModelId, newModelSource) = newViewModels[newItemPosition] as PersonViewModel
            return oldModelId == newModelId && oldModelSource == newModelSource
        }
        return false
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldViewModel = oldViewModels[oldItemPosition]
        val newViewModel = newViewModels[newItemPosition]

        if (oldViewModel is NoContactsViewModel && newViewModel is NoContactsViewModel) {
            return true
        }
        if (oldViewModel is FacebookImportViewModel && newViewModel is FacebookImportViewModel) {
            return oldViewModel == newViewModel
        }

        if (oldViewModel is PersonViewModel && newViewModel is PersonViewModel) {
            val oldPersonViewModel = oldViewModels[oldItemPosition] as PersonViewModel
            val newPersonViewModel = newViewModels[newItemPosition] as PersonViewModel

            return oldPersonViewModel == newPersonViewModel
        }
        return false
    }
}
