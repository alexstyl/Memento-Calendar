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
        composite +=
                Observable.zip(
                        contactSearch(searchResultView),
                        namedaySearch(searchResultView),
                        BiFunction<List<SearchResultViewModel>, NamedaySearchResultViewModel, List<SearchResultViewModel>>
                        { contactSearch: List<SearchResultViewModel>, namedaySearch: NamedaySearchResultViewModel ->
                            namedaySearch.asList() + contactSearch
                        })
                        .subscribeOn(workScheduler)
                        .observeOn(resultScheduler)
                        .subscribe { searchResultView.displaySearchResults(it) }

        if (namedayUserSettings.isEnabled) {
            composite +=
                    searchResultView.searchQueryObservable()
                            .map { query ->
                                namedayCalendar.allNames.filter { name ->
                                    name.asNameStartsWith(query)
                                            && name != query
                                }
                            }
                            .subscribeOn(workScheduler)
                            .observeOn(resultScheduler)
                            .subscribe {
                                searchResultView.displayNameSuggestions(it)
                            }
        }
    }

    private fun contactSearch(searchResultView: SearchResultView): Observable<List<ContactSearchResultViewModel>> {
        return searchResultView.searchQueryObservable()
                .debounce(DEBOUNCE_DURATION, TimeUnit.MILLISECONDS)
                .map { peopleEventsSearch.searchForContacts(it) }
                .map { viewModelFactory.viewModelsFor(it) }
    }

    private fun namedaySearch(searchResultView: SearchResultView): Observable<NamedaySearchResultViewModel> {
        return if (namedayUserSettings.isEnabled) {
            searchResultView.searchQueryObservable()
                    .map { searchQuery ->
                        namedayCalendar.getAllNamedays(searchQuery)
                    }.map {
                        NamedaySearchResultViewModel(it)
                    }
        } else {
            Observable.empty()
        }
    }

    fun stopPresenting() {
        composite?.dispose()
    }

    private fun NamedaySearchResultViewModel.asList() =
            if (namedays.dates.isNotEmpty()) {
                listOf(this)
            } else {
                emptyList()
            }


    private val namedayCalendar by lazy {
        val locale = namedayUserSettings.selectedLanguage
        val currentYear = todaysDate().year!!
        calendarProvider.loadNamedayCalendarForLocale(locale, currentYear)
    }


    companion object {
        private const val DEBOUNCE_DURATION = 200L
    }

    private fun String.asNameStartsWith(query: String) = comparator.startsWith(this, query)

}

