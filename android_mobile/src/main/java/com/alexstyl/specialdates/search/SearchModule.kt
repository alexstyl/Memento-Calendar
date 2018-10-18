package com.alexstyl.specialdates.search

import com.alexstyl.resources.Colors
import com.alexstyl.specialdates.Strings
import com.alexstyl.specialdates.analytics.Analytics
import com.alexstyl.specialdates.date.DateLabelCreator
import com.alexstyl.specialdates.date.todaysDate
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsProvider

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
                  viewModelFactory: ContactEventViewModelFactory): SearchPresenter {
        return SearchPresenter(peopleEventsSearch,
                viewModelFactory,
                Schedulers.io(),
                AndroidSchedulers.mainThread())
    }

    @Provides
    fun peopleEventsSearch(peopleEventsProvider: PeopleEventsProvider): PeopleEventsSearch {
        return PeopleEventsSearch(peopleEventsProvider, NameMatcher)
    }

    @Provides
    fun contactEventsViewModelFactory(contactEventLabelCreator: ContactEventLabelCreator,
                                      colors: Colors)
            : ContactEventViewModelFactory {
        return ContactEventViewModelFactory(contactEventLabelCreator, colors)
    }

    @Provides
    fun contactLabelCreator(strings: Strings, dateLabelCreator: DateLabelCreator): ContactEventLabelCreator {
        return ContactEventLabelCreator(todaysDate(), strings, dateLabelCreator)
    }
}
