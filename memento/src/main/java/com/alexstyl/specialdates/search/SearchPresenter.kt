package com.alexstyl.specialdates.search

import com.alexstyl.specialdates.NameComparator
import com.alexstyl.specialdates.date.todaysDate
import com.alexstyl.specialdates.events.namedays.NamedayUserSettings
import com.alexstyl.specialdates.events.namedays.calendar.resource.NamedayCalendarProvider
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.plusAssign
import java.util.concurrent.TimeUnit

class SearchPresenter(
        private val peopleEventsSearch: PeopleEventsSearch,
        private val viewModelFactory: SearchResultsViewModelFactory,
        private val namedayUserSettings: NamedayUserSettings,
        private val calendarProvider: NamedayCalendarProvider,
        private val comparator: NameComparator,
        private val workScheduler: Scheduler,
        private val resultScheduler: Scheduler) {

    private var composite = CompositeDisposable()

    fun presentInto(searchResultView: SearchResultView) {
        composite += subscribeToSearchResults(searchResultView)
        if (namedayUserSettings.isEnabled) {
            composite += subscribeToNameSuggestions(searchResultView)
        }
    }

    private fun subscribeToSearchResults(searchResultView: SearchResultView) =
            searchResultView
                    .searchQueryObservable
                    .debounce(DEBOUNCE_DURATION, TimeUnit.MILLISECONDS)
                    .switchMap { query ->
                        Observable.zip(
                                contactSearch(query),
                                namedaySearch(query),
                                BiFunction<List<SearchResultViewModel>, NamedaySearchResultViewModel, List<SearchResultViewModel>>
                                { contactSearch: List<SearchResultViewModel>, namedaySearch: NamedaySearchResultViewModel ->
                                    namedaySearch.asList() + contactSearch
                                })
                    }
                    .subscribeOn(workScheduler)
                    .observeOn(resultScheduler)
                    .subscribe { searchResultView.displaySearchResults(it) }

    private fun subscribeToNameSuggestions(searchResultView: SearchResultView) =
            searchResultView
                    .searchQueryObservable
                    .debounce(200, TimeUnit.MILLISECONDS)
                    .map { query ->
                        if (query.isNotEmpty()) {
                            namedayCalendar
                                    .allNames
                                    .filter { name ->
                                        name.asNameStartsWith(query) && name.asNameNotEquals(query)
                                    }
                        } else {
                            emptyList()
                        }
                    }
                    .subscribeOn(workScheduler)
                    .observeOn(resultScheduler)
                    .subscribe {
                        searchResultView.displayNameSuggestions(it)
                    }

    private fun NamedaySearchResultViewModel.asList() =
            if (namedays.dates.isNotEmpty()) {
                listOf(this)
            } else {
                emptyList()
            }


    private fun contactSearch(query: String) =
            Observable.fromCallable {
                peopleEventsSearch.searchForContacts(query)
            }
                    .map { viewModelFactory.viewModelsFor(it) }
                    .onErrorReturn { e ->
                        if (e is SecurityException) {
                            listOf(viewModelFactory.contactPermissionViewModel())
                        } else {
                            throw e
                        }
                    }

    private fun namedaySearch(searchResultView: String): Observable<NamedaySearchResultViewModel> {
        return if (namedayUserSettings.isEnabled) {
            Observable.fromCallable {
                namedayCalendar.getAllNamedays(searchResultView)
            }.map {
                NamedaySearchResultViewModel(it)
            }
        } else {
            Observable.empty()
        }
    }

    fun stopPresenting() {
        composite.clear()
    }


    private val namedayCalendar by lazy {
        val locale = namedayUserSettings.selectedLanguage
        val currentYear = todaysDate().year!!
        calendarProvider.loadNamedayCalendarForLocale(locale, currentYear)
    }


    private fun String.asNameStartsWith(query: String) = comparator.startsWith(this, query)

    private fun String.asNameNotEquals(query: String) = !comparator.compare(this, query)

    companion object {
        private const val DEBOUNCE_DURATION = 200L

    }

}



