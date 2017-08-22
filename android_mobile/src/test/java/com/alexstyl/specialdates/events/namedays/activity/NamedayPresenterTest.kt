package com.alexstyl.specialdates.events.namedays.activity

import com.alexstyl.specialdates.contact.ContactFixture.aContactCalled
import com.alexstyl.specialdates.contact.ContactsProvider
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.Months.JANUARY
import com.alexstyl.specialdates.events.namedays.NamesInADate
import com.alexstyl.specialdates.events.namedays.calendar.NamedayCalendar
import com.alexstyl.specialdates.ui.widget.LetterPainter
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class NamedayPresenterTest {

    private val mockView = Mockito.mock(NamedaysMVPView::class.java)
    private val LETTER_VARIANT = 5

    private val CHECKING_DATE = Date.on(1, JANUARY, 2017)
    private val mockNamedayCalendar = Mockito.mock(NamedayCalendar::class.java)
    private var mockContactsProvider = Mockito.mock(ContactsProvider::class.java)
    private var mockLetterPainter = Mockito.mock(LetterPainter::class.java)

    private lateinit var presenter: NamedayPresenter

    @Before
    fun setUp() {
        val workScheduler = Schedulers.trampoline()
        val resultScheduler = Schedulers.trampoline()
        presenter = NamedayPresenter(mockNamedayCalendar, NamedaysViewModelFactory(mockLetterPainter), mockContactsProvider, workScheduler, resultScheduler)
        Mockito.`when`(mockLetterPainter.getVariant(Mockito.anyInt())).thenReturn(LETTER_VARIANT)
    }

    @Test
    fun whenNoNamedaysExistOnASpecificDate_thenNoViewModelsArePassedToTheView() {
        Mockito.`when`(mockNamedayCalendar.getAllNamedayOn(CHECKING_DATE)).thenReturn(NamesInADate(CHECKING_DATE, emptyList()))

        presenter.startPresenting(mockView, forDate = CHECKING_DATE)

        Mockito.verify(mockView).displayNamedays(emptyList())
    }

    @Test
    fun aNamedayWithAContact_returnsAViewModelWithThatContact() {
        Mockito.`when`(mockContactsProvider.allContacts).thenReturn(arrayListOf(aContactCalled("Kate Brown")))
        Mockito.`when`(mockNamedayCalendar.getAllNamedayOn(CHECKING_DATE)).thenReturn(NamesInADate(CHECKING_DATE, arrayListOf("Kate")))

        presenter.startPresenting(mockView, forDate = CHECKING_DATE)

        Mockito.verify(mockView).displayNamedays(
                arrayListOf(
                        NamedaysViewModel("Kate"),
                        CelebratingContactViewModel(aContactCalled("Kate Brown"), "Kate Brown", LETTER_VARIANT)
                ))
    }

    @Test
    fun aNamedayWithoutRelatedContacts_returnsOnlyTheNameday() {
        Mockito.`when`(mockContactsProvider.allContacts).thenReturn(emptyList())
        Mockito.`when`(mockNamedayCalendar.getAllNamedayOn(CHECKING_DATE)).thenReturn(NamesInADate(CHECKING_DATE, arrayListOf("Kate")))

        presenter.startPresenting(mockView, forDate = CHECKING_DATE)

        val expectedViewModels = arrayListOf(NamedaysViewModel("Kate"))
        Mockito.verify(mockView).displayNamedays(expectedViewModels)
    }

}
