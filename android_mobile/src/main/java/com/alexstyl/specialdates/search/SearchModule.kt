package com.alexstyl.specialdates.search

import com.alexstyl.resources.Colors
import com.alexstyl.specialdates.Strings
import com.alexstyl.specialdates.analytics.Analytics
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
    fun navigator(analytics: Analytics): SearchNavigator {
        return SearchNavigator(analytics)
    }

    @Provides
    fun presenter(peopleEventsSearch: PeopleEventsSearch,
                  viewModelFactory: SearchResultsViewModelFactory,
                  namedayUserSettings: NamedayUserSettings,
                  calendarProvider: NamedayCalendarProvider): SearchPresenter {
        return SearchPresenter(peopleEventsSearch,
                viewModelFactory,
                namedayUserSettings,
                calendarProvider,
                Schedulers.io(),
                AndroidSchedulers.mainThread())
    }

    @Provides
    fun peopleEventsSearch(contactsProvider: ContactsProvider): PeopleEventsSearch {
        return PeopleEventsSearch(contactsProvider, NameMatcher)
    }

    @Provides
    fun contactEventsViewModelFactory(contactEventLabelCreator: ContactEventLabelCreator,
                                      colors: Colors)
            : SearchResultsViewModelFactory {
        return SearchResultsViewModelFactory(contactEventLabelCreator, colors)
    }

    @Provides
    fun contactLabelCreator(strings: Strings, dateLabelCreator: DateLabelCreator): ContactEventLabelCreator {
        return ContactEventLabelCreator(todaysDate(), strings, dateLabelCreator)
    }
}
