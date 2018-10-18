package com.alexstyl.specialdates.search

import io.reactivex.Observable

interface SearchResultView {
    fun showResults(result: SearchResults)

    fun searchQueryObservable(): Observable<String>
}
