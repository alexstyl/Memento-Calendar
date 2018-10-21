package com.alexstyl.specialdates.search

import io.reactivex.Observable

class AndroidSearchResultView(private val resultsAdapter: SearchResultsAdapter,
                              private val searchbar: SearchToolbar) : SearchResultView {
    override fun showSearchResults(it: List<SearchResultViewModel>) {
        resultsAdapter.displaySearchResults(it)
    }

    override fun searchQueryObservable() = Observable.create<String> { emitter ->
        searchbar.addTextWatcher { text ->
            emitter.onNext(text)
        }
    }!!

}
