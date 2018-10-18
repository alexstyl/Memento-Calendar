package com.alexstyl.specialdates.search

data class SearchResults(val viewModels: List<ContactEventViewModel>,
                         val canLoadMore: Boolean)