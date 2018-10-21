package com.alexstyl.specialdates.search

import io.reactivex.Observable

interface SearchResultView {
    fun searchQueryObservable(): Observable<String>
    fun displaySearchResults(it: List<SearchResultViewModel>)
    fun displayNameSuggestions(names: List<String>)
}
