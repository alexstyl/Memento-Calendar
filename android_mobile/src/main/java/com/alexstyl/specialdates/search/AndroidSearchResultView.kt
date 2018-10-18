package com.alexstyl.specialdates.search

import io.reactivex.Observable

class AndroidSearchResultView(private val resultsAdapter: SearchResultsAdapter,
                              private val searchbar: SearchToolbar) : SearchResultView {

    override fun searchQueryObservable() = Observable.create<String> { emitter ->
        searchbar.addTextWatcher { text ->
            emitter.onNext(text)
        }
    }

    override fun showResults(result: SearchResults) {
        resultsAdapter.updateSearchResults(result)
    }
}
