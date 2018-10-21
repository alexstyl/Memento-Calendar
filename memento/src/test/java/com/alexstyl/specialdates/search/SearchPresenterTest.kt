package com.alexstyl.specialdates.search

import com.alexstyl.TestColors
import com.alexstyl.specialdates.JavaStrings
import com.alexstyl.specialdates.TestContactEventsBuilder
import com.alexstyl.specialdates.TestDateLabelCreator
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.contact.ContactSource
import com.alexstyl.specialdates.contact.ContactSource.SOURCE_DEVICE
import com.alexstyl.specialdates.contact.ContactsProvider
import com.alexstyl.specialdates.contact.ContactsProviderSource
import com.alexstyl.specialdates.contact.DisplayName
import com.alexstyl.specialdates.date.Months.DECEMBER
import com.alexstyl.specialdates.date.Months.OCTOBER
import com.alexstyl.specialdates.date.dateOn
import com.alexstyl.specialdates.events.namedays.NamedayUserSettings
import com.alexstyl.specialdates.events.namedays.calendar.resource.NamedayCalendarProvider
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsProvider
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.verify
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.internal.verification.Times
import java.util.concurrent.TimeUnit

class SearchPresenterTest {

    private lateinit var presenter: SearchPresenter

    private val mockContactSource = mock(ContactsProviderSource::class.java)


    private val mockNamedayUserSettings = mock(NamedayUserSettings::class.java)
    private val mockNamedayCalendarProvider = mock(NamedayCalendarProvider::class.java)
    private val mockView = mock(SearchResultView::class.java)

    @Before
    fun setUp() {
        presenter = SearchPresenter(
                PeopleEventsSearch(ContactsProvider(mapOf(Pair(SOURCE_DEVICE, mockContactSource))), NameMatcher),
                SearchResultsViewModelFactory(
                        ContactEventLabelCreator(ANY_DATE, JavaStrings(), TestDateLabelCreator.forUS()),
                        TestColors()
                ),
                mockNamedayUserSettings,
                mockNamedayCalendarProvider,
                Schedulers.trampoline(),
                Schedulers.trampoline()
        )
        given(mockView.searchQueryObservable()).willReturn(Observable.empty())
    }

    @Test
    fun givenSubsequentTextChanges_thenOnlyTheLastOneGetsDelivered() {
//        val date = dateOn(1, DECEMBER, 2017)

        val searchQueryObservable = PublishSubject.create<String>()
        given(mockView.searchQueryObservable()).willReturn(searchQueryObservable)
        given(mockContactSource.allContacts).willReturn(
                listOf(
                        ANY_CONTACT.copy(contactID = 0, displayName = DisplayName.from("text1")),
                        ANY_CONTACT.copy(contactID = 1, displayName = DisplayName.from("text2")),
                        ANY_CONTACT.copy(contactID = 2, displayName = DisplayName.from("text3")),
                        ANY_CONTACT.copy(contactID = 3, displayName = DisplayName.from("text4")),
                        ANY_CONTACT.copy(contactID = 4, displayName = DisplayName.from("text5"))
                ))

        presenter.presentInto(mockView)
        searchQueryObservable.onNext("text5")
        verify(mockView, Times(1)).showSearchResults(
                listOf(ContactSearchResultViewModel(
                        ANY_CONTACT.copy(contactID = 4, displayName = DisplayName.from("text5")),
                        "text5", "", "Birthday on December 1", 0, 4)))
    }


    @Test
    fun givenASearchQuery_thenTheContactWithThatNameIsReturned() {
        val searchQueryObservable = PublishSubject.create<String>()
        given(mockView.searchQueryObservable()).willReturn(searchQueryObservable)

        val contact = ANY_CONTACT.copy(displayName = DisplayName.from("Alex"))

        given(mockPeopleEventsProvider.allContacts).willReturn(listOf(contact))

        presenter.presentInto(mockView)
        searchQueryObservable.onNext("Alex")

        Mockito.verify(mockView).showSearchResults(
                listOf(ContactSearchResultViewModel(ANY_CONTACT.copy(displayName = DisplayName.from("Alex")), "Alex", "", "Nameday on October 18", 1, -1))
        )
    }


    companion object {
        val ANY_DATE = dateOn(1, DECEMBER, 2017)
        val ANY_CONTACT = Contact(-1, DisplayName.from(""), "", ContactSource.SOURCE_DEVICE)
    }
}