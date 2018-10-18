package com.alexstyl.specialdates.search

import com.alexstyl.specialdates.events.namedays.NameCelebrations
import io.reactivex.Observable

class AndroidSearchResultView(private val resultsAdapter: SearchResultsAdapter,
                              private val searchbar: SearchToolbar) : SearchResultView {

    override fun showNamedayResults(result: NameCelebrations) {
        resultsAdapter.setNamedays(result)
    }

    override fun searchQueryObservable() = Observable.create<String> { emitter ->
        searchbar.addTextWatcher { text ->
            emitter.onNext(text)
        }
    }

    override fun showContactResults(result: SearchResults) {
        resultsAdapter.updateSearchResults(result)
    }
}
