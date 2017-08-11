package com.alexstyl.specialdates.events.namedays.activity

import com.alexstyl.specialdates.contact.ContactFixture
import com.alexstyl.specialdates.contact.ContactsProvider
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.Months.JANUARY
import com.alexstyl.specialdates.events.namedays.NamesInADate
import com.alexstyl.specialdates.events.namedays.calendar.NamedayCalendar
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class NamedayPresenterTest {

    private val mockView = Mockito.mock(NamedaysMVPView::class.java)

    private val CHECKING_DATE = Date.on(1, JANUARY, 2017)
    private val mockNamedayCalendar = Mockito.mock(NamedayCalendar::class.java)
    private val mockContactsProvider = Mockito.mock(ContactsProvider::class.java)

    private lateinit var presenter: NamedayPresenter

    @Before
    fun setUp() {
        presenter = NamedayPresenter(mockNamedayCalendar, NamedaysViewModelFactory(mockContactsProvider))
    }

    @Test
    fun assertThatTheViewGetsPopulatedCorrectly() {
        val celebratingContacts = arrayListOf(ContactFixture.withName("Kate Brown"))
        Mockito.`when`(mockContactsProvider.contactsCalled("Kate")).thenReturn(celebratingContacts)
        Mockito.`when`(mockNamedayCalendar.getAllNamedayOn(CHECKING_DATE)).thenReturn(NamesInADate(CHECKING_DATE, arrayListOf("Kate")))

        presenter.startPresentingInto(mockView, forDate = CHECKING_DATE)

        val expectedViewModels = arrayListOf(NamedaysViewModel("Kate", celebratingContacts))
        Mockito.verify(mockView).displayNamedays(expectedViewModels)
    }
}
