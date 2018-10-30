package com.alexstyl.specialdates.search

import com.alexstyl.specialdates.PhoneticComparator
import com.alexstyl.specialdates.Strings
import com.alexstyl.specialdates.contact.ContactsProvider
import com.alexstyl.specialdates.date.DateLabelCreator
import com.alexstyl.specialdates.date.todaysDate
import com.alexstyl.specialdates.events.namedays.NamedayUserSettings
import com.alexstyl.specialdates.events.namedays.calendar.resource.NamedayCalendarProvider
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsProvider

import dagger.Module
import dagger.Provides
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@Module
class SearchModule {

    @Provides
    fun presenter(peopleSearch: PeopleSearch,
                  viewModelFactory: SearchResultsViewModelFactory,
                  namedayUserSettings: NamedayUserSettings,
                  calendarProvider: NamedayCalendarProvider): SearchPresenter {
        return SearchPresenter(peopleSearch,
                viewModelFactory,
                namedayUserSettings,
                calendarProvider,
                PhoneticComparator(),
                Schedulers.io(),
                AndroidSchedulers.mainThread())
    }

    @Provides
    fun peopleEventsSearch(contactsProvider: PeopleEventsProvider): PeopleSearch {
        return PeopleSearch(contactsProvider, NameMatcher)
    }

    @Provides
    fun contactEventsViewModelFactory(labelCreator: DateLabelCreator): SearchResultsViewModelFactory {
        return SearchResultsViewModelFactory(labelCreator)
    }

    @Provides
    fun contactLabelCreator(strings: Strings, dateLabelCreator: DateLabelCreator): ContactEventLabelCreator {
        return ContactEventLabelCreator(todaysDate(), strings, dateLabelCreator)
    }
}
