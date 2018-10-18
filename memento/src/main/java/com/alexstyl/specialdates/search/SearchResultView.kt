package com.alexstyl.specialdates.search

import com.alexstyl.specialdates.events.namedays.NameCelebrations
import io.reactivex.Observable

interface SearchResultView {
    fun showContactResults(result: SearchResults)
    fun searchQueryObservable(): Observable<String>
    fun showNamedayResults(result: NameCelebrations)
}
