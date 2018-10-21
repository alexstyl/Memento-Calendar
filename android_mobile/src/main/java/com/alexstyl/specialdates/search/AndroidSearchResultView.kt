package com.alexstyl.specialdates.search

import io.reactivex.Observable

class AndroidSearchResultView(private val resultsAdapter: SearchResultsAdapter,
                              private val searchbar: SearchToolbar,
                              private val namesAdapter: NameSuggestionsAdapter?) : SearchResultView {

    override fun searchQueryObservable() = Observable.create<String> { emitter ->
        searchbar.addTextWatcher { text ->
            emitter.onNext(text)
        }
    }!!

    override fun displaySearchResults(it: List<SearchResultViewModel>) {
        resultsAdapter.displaySearchResults(it)
    }

    override fun displayNameSuggestions(names: List<String>) {
        namesAdapter?.updateNames(names)
    }
}
