package com.alexstyl.specialdates.search

import io.reactivex.Observable

interface SearchResultView {
    fun searchQueryObservable(): Observable<String>
    fun showSearchResults(it: List<SearchResultViewModel>)
}
