package com.alexstyl.specialdates.search

data class SearchResults(val viewModels: List<ContactSearchResultViewModel>,
                         val canLoadMore: Boolean)