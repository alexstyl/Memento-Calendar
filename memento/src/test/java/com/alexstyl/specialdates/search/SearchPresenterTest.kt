package com.alexstyl.specialdates.search

import com.alexstyl.TestColors
import com.alexstyl.specialdates.JavaStrings
import com.alexstyl.specialdates.TestContactEventsBuilder
import com.alexstyl.specialdates.TestDateLabelCreator
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.contact.ContactSource
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

    private val mockPeopleEventsProvider = mock(PeopleEventsProvider::class.java)
    private val mockNamedayUserSettings = mock(NamedayUserSettings::class.java)
    private val mockNamedayCalendarProvider = mock(NamedayCalendarProvider::class.java)
    private val mockView = mock(SearchResultView::class.java)
    private val testScheduler = TestScheduler()

    @Before
    fun setUp() {
        presenter = SearchPresenter(
                PeopleEventsSearch(mockPeopleEventsProvider, NameMatcher),
                SearchResultsViewModelFactory(
                        ContactEventLabelCreator(ANY_DATE, JavaStrings(), TestDateLabelCreator.forUS()),
                        TestColors()
                ),
                mockNamedayUserSettings,
                mockNamedayCalendarProvider,
                Schedulers.trampoline(),
                testScheduler
        )
        given(mockView.searchQueryObservable()).willReturn(Observable.empty())
    }

    @Test
    fun givenSubsequentTextChanges_thenOnlyTheLastOneGetsDelivered() {
        val date = dateOn(1, DECEMBER, 2017)

        val searchQueryObservable = PublishSubject.create<String>()
        given(mockView.searchQueryObservable()).willReturn(searchQueryObservable)
        given(mockPeopleEventsProvider.fetchAllEventsInAYear()).willReturn(
                TestContactEventsBuilder()
                        .addBirthdayFor(ANY_CONTACT.copy(contactID = 0, displayName = DisplayName.from("text1")), date)
                        .addBirthdayFor(ANY_CONTACT.copy(contactID = 1, displayName = DisplayName.from("text2")), date)
                        .addBirthdayFor(ANY_CONTACT.copy(contactID = 2, displayName = DisplayName.from("text3")), date)
                        .addBirthdayFor(ANY_CONTACT.copy(contactID = 3, displayName = DisplayName.from("text4")), date)
                        .addBirthdayFor(ANY_CONTACT.copy(contactID = 4, displayName = DisplayName.from("text5")), date)
                        .build()
        )

        presenter.presentInto(mockView)

        searchQueryObservable.onNext("text1")
        testScheduler.advanceTimeBy(20, TimeUnit.MILLISECONDS)

        searchQueryObservable.onNext("text2")
        testScheduler.advanceTimeBy(20, TimeUnit.MILLISECONDS)

        searchQueryObservable.onNext("text3")
        testScheduler.advanceTimeBy(20, TimeUnit.MILLISECONDS)

        searchQueryObservable.onNext("text4")
        testScheduler.advanceTimeBy(20, TimeUnit.MILLISECONDS)

        searchQueryObservable.onNext("text5")
        testScheduler.advanceTimeBy(20, TimeUnit.MILLISECONDS)

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        verify(mockView, Times(1)).showContactResults(SearchResults(listOf(
                ContactEventViewModel(
                        ANY_CONTACT.copy(contactID = 4, displayName = DisplayName.from("text5")),
                        "text5", "", "Birthday on December 1", 0, 4)), false))
    }


    @Test
    fun givenASearchQuery_thenTheContactWithThatNameIsReturned() {
        val searchQueryObservable = PublishSubject.create<String>()
        given(mockView.searchQueryObservable()).willReturn(searchQueryObservable)

        val contact = ANY_CONTACT.copy(displayName = DisplayName.from("Alex"))

        given(mockPeopleEventsProvider.fetchAllEventsInAYear()).willReturn(
                TestContactEventsBuilder().addNamedayFor(contact, dateOn(18, OCTOBER, 2017)).build()
        )

        presenter.presentInto(mockView)
        searchQueryObservable.onNext("Alex")

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        Mockito.verify(mockView).showContactResults(
                SearchResults(listOf(ContactEventViewModel(ANY_CONTACT.copy(displayName = DisplayName.from("Alex")), "Alex", "", "Nameday on October 18", 1, -1)), false)
        )
    }


    companion object {
        val ANY_DATE = dateOn(1, DECEMBER, 2017)
        val ANY_CONTACT = Contact(-1, DisplayName.from(""), "", ContactSource.SOURCE_DEVICE)
    }
}