package com.alexstyl.specialdates.search

import com.alexstyl.specialdates.PhoneticComparator
import com.alexstyl.specialdates.TestDateLabelCreator
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.contact.ContactSource
import com.alexstyl.specialdates.contact.ContactSource.SOURCE_DEVICE
import com.alexstyl.specialdates.contact.ContactsProvider
import com.alexstyl.specialdates.contact.ContactsProviderSource
import com.alexstyl.specialdates.contact.DisplayName
import com.alexstyl.specialdates.events.namedays.NamedayUserSettings
import com.alexstyl.specialdates.events.namedays.calendar.resource.NamedayCalendarProvider
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.verify
import org.mockito.Mockito.mock
import org.mockito.internal.verification.Times
import java.util.concurrent.TimeUnit

class SearchPresenterTest {

    private lateinit var presenter: SearchPresenter

    private val mockContactSource = mock(ContactsProviderSource::class.java)


    private val mockNamedayUserSettings = mock(NamedayUserSettings::class.java)
    private val mockNamedayCalendarProvider = mock(NamedayCalendarProvider::class.java)
    private val mockView = mock(SearchResultView::class.java)


    private val testScheduler = TestScheduler()
    @Before
    fun setUp() {
        presenter = SearchPresenter(
                PeopleEventsSearch(ContactsProvider(mapOf(Pair(SOURCE_DEVICE, mockContactSource))), NameMatcher),
                SearchResultsViewModelFactory(TestDateLabelCreator.forUS()),
                mockNamedayUserSettings,
                mockNamedayCalendarProvider,
                PhoneticComparator(),
                testScheduler,
                Schedulers.trampoline()
        )
        given(mockView.searchQueryObservable).willReturn(Observable.empty())
        given(mockNamedayUserSettings.isEnabled).willReturn(false)
    }

    @Test
    fun givenSubsequentTextChanges_thenOnlyTheLastOneGetsDelivered() {
        val searchQueryObservable = PublishSubject.create<String>()
        given(mockView.searchQueryObservable).willReturn(searchQueryObservable)
        given(mockContactSource.allContacts).willReturn(
                listOf(
                        aContact.copy(contactID = 0, displayName = DisplayName.from("text1")),
                        aContact.copy(contactID = 1, displayName = DisplayName.from("text2")),
                        aContact.copy(contactID = 2, displayName = DisplayName.from("text3")),
                        aContact.copy(contactID = 3, displayName = DisplayName.from("text4")),
                        aContact.copy(contactID = 4, displayName = DisplayName.from("text5"))
                ))

        presenter.presentInto(mockView)
        testScheduler.triggerActions()
        searchQueryObservable.onNext("text5")
        testScheduler.advanceTimeBy(200, TimeUnit.MILLISECONDS)

        verify(mockView, Times(1)).displaySearchResults(
                listOf(ContactSearchResultViewModel(
                        aContact.copy(contactID = 4, displayName = DisplayName.from("text5")),
                        "text5", "", 4)))
    }

    @Test
    fun givenASearchQuery_thenTheContactWithThatNameIsReturned() {
        val searchQueryObservable = PublishSubject.create<String>()
        given(mockView.searchQueryObservable).willReturn(searchQueryObservable)
        given(mockContactSource.allContacts).willReturn(listOf(aContact.called("Alex")))

        presenter.presentInto(mockView)
        testScheduler.triggerActions()
        searchQueryObservable.onNext("Alex") // user typed "Alex"
        testScheduler.advanceTimeBy(200, TimeUnit.MILLISECONDS)

        verify(mockView).displaySearchResults(
                listOf(ContactSearchResultViewModel(aContact.copy(displayName = DisplayName.from("Alex")), "Alex", "", -1))
        )
    }


    companion object {
        val aContact = Contact(-1, DisplayName.from(""), "", ContactSource.SOURCE_DEVICE)
    }

    private fun Contact.called(name: String): Contact {
        return copy(displayName = DisplayName.from(name))
    }
}

