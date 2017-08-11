package com.alexstyl.specialdates.events.namedays.activity

import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.contact.ContactFixture.aContactCalled
import com.alexstyl.specialdates.contact.ContactsProvider
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.Months.JANUARY
import com.alexstyl.specialdates.events.namedays.NamesInADate
import com.alexstyl.specialdates.events.namedays.calendar.NamedayCalendar
import io.reactivex.schedulers.Schedulers
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
    private var mockContactsProvider = Mockito.mock(ContactsProvider::class.java)

    private lateinit var presenter: NamedayPresenter

    @Before
    fun setUp() {
        val workScheduler = Schedulers.trampoline()
        val resultScheduler = Schedulers.trampoline()
        presenter = NamedayPresenter(mockNamedayCalendar, NamedaysViewModelFactory(mockContactsProvider), workScheduler, resultScheduler)
    }

    @Test
    fun whenNoNamedaysExistOnASpecificDate_thenNoViewModelsArePassedToTheView() {
        Mockito.`when`(mockNamedayCalendar.getAllNamedayOn(CHECKING_DATE)).thenReturn(NamesInADate(CHECKING_DATE, emptyList()))

        presenter.startPresenting(mockView, forDate = CHECKING_DATE)

        Mockito.verify(mockView).displayNamedays(emptyList())
    }

    @Test
    fun aNamedayWithAContact_returnsAViewModelWithThatContact() {
        Mockito.`when`(mockContactsProvider.contactsCalled("Kate")).thenReturn(arrayListOf(aContactCalled("Kate Brown")))
        Mockito.`when`(mockNamedayCalendar.getAllNamedayOn(CHECKING_DATE)).thenReturn(NamesInADate(CHECKING_DATE, arrayListOf("Kate")))

        presenter.startPresenting(mockView, forDate = CHECKING_DATE)

        Mockito.verify(mockView).displayNamedays(
                arrayListOf(
                        NamedaysViewModel("Kate", aContactCalled("Kate Brown"))
                ))
    }

    @Test
    fun aNamedayWithoutRelatedContacts_returnsOnlyTheNameday() {
        Mockito.`when`(mockContactsProvider.contactsCalled(Mockito.anyString())).thenReturn(emptyList())
        Mockito.`when`(mockNamedayCalendar.getAllNamedayOn(CHECKING_DATE)).thenReturn(NamesInADate(CHECKING_DATE, arrayListOf("Kate")))

        presenter.startPresenting(mockView, forDate = CHECKING_DATE)

        val expectedViewModels = arrayListOf(NamedaysViewModel("Kate", emptyList()))
        Mockito.verify(mockView).displayNamedays(expectedViewModels)
    }

}

private fun NamedaysViewModel(name: String, contact: Contact): NamedaysViewModel = NamedaysViewModel(name, arrayListOf(contact))

