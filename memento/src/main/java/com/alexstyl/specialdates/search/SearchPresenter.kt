package com.alexstyl.specialdates.search

import com.alexstyl.specialdates.date.todaysDate
import com.alexstyl.specialdates.events.namedays.NamedayUserSettings
import com.alexstyl.specialdates.events.namedays.calendar.NamedayCalendar
import com.alexstyl.specialdates.events.namedays.calendar.resource.NamedayCalendarProvider
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import java.util.concurrent.TimeUnit

class SearchPresenter(
        private val peopleEventsSearch: PeopleEventsSearch,
        private val viewModelFactory: SearchResultsViewModelFactory,
        private val namedayUserSettings: NamedayUserSettings,
        private val calendarProvider: NamedayCalendarProvider,
        private val workScheduler: Scheduler,
        private val resultScheduler: Scheduler) {

    private var composite: Disposable? = null

    fun presentInto(searchResultView: SearchResultView) {
        composite =
                Observable.zip(
                        contactSearch(searchResultView),
                        namedaySearch(searchResultView),
                        BiFunction<List<SearchResultViewModel>, NamedaySearchResultViewModel, List<SearchResultViewModel>>
                        { contactSearch: List<SearchResultViewModel>, namedaySearch: NamedaySearchResultViewModel ->
                            namedaySearch.asList() + contactSearch
                        })
                        .subscribeOn(workScheduler)
                        .observeOn(resultScheduler)
                        .subscribe { searchResultView.showSearchResults(it) }
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
                        namedayCalendar().getAllNamedays(searchQuery)
                    }.map {
                        NamedaySearchResultViewModel(it)
                    }
        } else {
            Observable.empty()
        }
    }

    private fun namedayCalendar(): NamedayCalendar {
        val locale = namedayUserSettings.selectedLanguage
        val currentYear = todaysDate().year!!
        return calendarProvider.loadNamedayCalendarForLocale(locale, currentYear)
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


    companion object {
        private const val DEBOUNCE_DURATION = 200L
    }
}
