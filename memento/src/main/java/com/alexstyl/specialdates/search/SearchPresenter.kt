package com.alexstyl.specialdates.search

import com.alexstyl.specialdates.date.todaysDate
import com.alexstyl.specialdates.events.namedays.NamedayUserSettings
import com.alexstyl.specialdates.events.namedays.calendar.NamedayCalendar
import com.alexstyl.specialdates.events.namedays.calendar.resource.NamedayCalendarProvider
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class SearchPresenter(
        private val peopleEventsSearch: PeopleEventsSearch,
        private val viewModelFactory: SearchResultsViewModelFactory,
        private val namedayUserSettings: NamedayUserSettings,
        private val calendarProvider: NamedayCalendarProvider,
        private val workScheduler: Scheduler,
        private val resultScheduler: Scheduler) {


    private val composite = CompositeDisposable()

    fun presentInto(searchResultView: SearchResultView) {
        composite.add(
                searchResultView.searchQueryObservable()
                        .debounce(DEBOUNCE_DURATION, TimeUnit.MILLISECONDS, resultScheduler)
                        .map { peopleEventsSearch.searchForContacts(it, searchCounter) }
                        .map { viewModelFactory.viewModelsFor(it) }
                        .map { SearchResults(it, it.size > searchCounter) }
                        .subscribeOn(workScheduler)
                        .observeOn(resultScheduler)
                        .subscribe { searchResultView.showContactResults(it) }
        )

        if (namedayUserSettings.isEnabled) {
            composite.add(
                    searchResultView.searchQueryObservable()
                            .map { searchQuery ->
                                val calendar = namedayCalendar()
                                calendar.getAllNamedays(searchQuery)
                            }
                            .subscribeOn(workScheduler)
                            .observeOn(resultScheduler)
                            .subscribe { searchResultView.showNamedayResults(it) }

            )
        }
    }

    private fun namedayCalendar(): NamedayCalendar {
        val locale = namedayUserSettings.selectedLanguage
        val currentYear = todaysDate().year!!
        val calendar = calendarProvider.loadNamedayCalendarForLocale(locale, currentYear)
        return calendar
    }

    fun stopPresenting() {
        composite.dispose()
    }

    companion object {
        private const val searchCounter = 20
        private const val DEBOUNCE_DURATION = 200L
    }


}
