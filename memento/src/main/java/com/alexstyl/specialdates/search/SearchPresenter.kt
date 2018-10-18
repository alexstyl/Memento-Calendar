package com.alexstyl.specialdates.search

import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class SearchPresenter(
        private val peopleEventsSearch: PeopleEventsSearch,
        private val viewModelFactory: ContactEventViewModelFactory,
        private val workScheduler: Scheduler,
        private val resultScheduler: Scheduler) {

    private val composite = CompositeDisposable()

    fun presentInto(searchResultView: SearchResultView) {
        composite.add(
                searchResultView.searchQueryObservable()
                        .debounce(200, TimeUnit.MILLISECONDS, resultScheduler)
                        .map { peopleEventsSearch.searchForContacts(it, searchCounter) }
                        .map { viewModelFactory.createViewModelFrom(it) }
                        .map { SearchResults(it, it.size > searchCounter) }
                        .subscribeOn(workScheduler)
                        .observeOn(resultScheduler)
                        .subscribe { results -> searchResultView.showResults(results) }
        )
        // TODO nameday search
    }

    fun stopPresenting() {
        composite.dispose()
    }

    companion object {
        private const val searchCounter = 20
    }


}
