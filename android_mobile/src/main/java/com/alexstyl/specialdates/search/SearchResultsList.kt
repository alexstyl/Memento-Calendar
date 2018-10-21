package com.alexstyl.specialdates.search

class SearchResultsList {

    private var namedayCardViewModels: SearchResultViewModel? = null
    private val contactViewModels = mutableListOf<SearchResultViewModel>()

    fun setContactResults(viewModels: List<ContactSearchResultViewModel>) {
        this.contactViewModels.clear()
        this.contactViewModels.addAll(viewModels)
    }

    fun setNameday(namedays: NamedaySearchResultViewModel) {
        namedayCardViewModels = namedays
    }

    operator fun get(position: Int): SearchResultViewModel {
        return if (namedayCardViewModels != null && position == 0) {
            namedayCardViewModels!!
        } else {
            contactViewModels[if (namedayCardViewModels == null) position else position + 1]
        }
    }

    fun size() = contactViewModels.size + namedayCardViewModels.countUnlessNull()

    private fun SearchResultViewModel?.countUnlessNull() = if (this == null) 0 else 1
}

