package com.alexstyl.specialdates.search

import com.alexstyl.specialdates.PhoneticComparator
import com.alexstyl.specialdates.Strings
import com.alexstyl.specialdates.contact.ContactsProvider
import com.alexstyl.specialdates.date.DateLabelCreator
import com.alexstyl.specialdates.date.todaysDate
import com.alexstyl.specialdates.events.namedays.NamedayUserSettings
import com.alexstyl.specialdates.events.namedays.calendar.resource.NamedayCalendarProvider

import dagger.Module
import dagger.Provides
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@Module
class SearchModule {

    @Provides
    fun presenter(peopleEventsSearch: PeopleEventsSearch,
                  viewModelFactory: SearchResultsViewModelFactory,
                  namedayUserSettings: NamedayUserSettings,
                  calendarProvider: NamedayCalendarProvider): SearchPresenter {
        return SearchPresenter(peopleEventsSearch,
                viewModelFactory,
                namedayUserSettings,
                calendarProvider,
                PhoneticComparator(),
                Schedulers.io(),
                AndroidSchedulers.mainThread())
    }

    @Provides
    fun peopleEventsSearch(contactsProvider: ContactsProvider): PeopleEventsSearch {
        return PeopleEventsSearch(contactsProvider, NameMatcher)
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
