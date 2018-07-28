package com.alexstyl.specialdates.events.namedays.activity

import com.alexstyl.specialdates.addevent.ui.willReturnContacts
import com.alexstyl.specialdates.addevent.ui.willReturnNoContact
import com.alexstyl.specialdates.contact.ContactFixture.aContactCalled
import com.alexstyl.specialdates.contact.ContactsProvider
import com.alexstyl.specialdates.contact.ContactsProviderSource
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.Months.JANUARY
import com.alexstyl.specialdates.events.namedays.NamedayUserSettings
import com.alexstyl.specialdates.events.namedays.NoNamesInADate
import com.alexstyl.specialdates.events.namedays.calendar.ImmutableNamesInADate
import com.alexstyl.specialdates.events.namedays.calendar.NamedayCalendar
import com.alexstyl.specialdates.ui.widget.LetterPainter
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class NamedaysInADayPresenterTest {

    private val mockView = Mockito.mock(NamedaysOnADayView::class.java)
    private val LETTER_VARIANT = 5

    private val ANY_DATE = Date.on(1, JANUARY, 2017)
    private val mockNamedayCalendar = Mockito.mock(NamedayCalendar::class.java)
    private var mockSource = Mockito.mock(ContactsProviderSource::class.java)
    private var mockLetterPainter = Mockito.mock(LetterPainter::class.java)
    private val mockUserSettings = Mockito.mock(NamedayUserSettings::class.java)

    private lateinit var presenter: NamedaysInADayPresenter

    @Before
    fun setUp() {
        val workScheduler = Schedulers.trampoline()
        val resultScheduler = Schedulers.trampoline()

        presenter = NamedaysInADayPresenter(
                mockNamedayCalendar,
                NamedaysViewModelFactory(mockLetterPainter),
                ContactsProvider(mapOf(Pair(0, mockSource))), mockUserSettings, workScheduler, resultScheduler)
        given(mockLetterPainter.getVariant(Mockito.anyInt())).willReturn(LETTER_VARIANT)
        given(mockSource.allContacts).willReturnNoContact()
    }

    @Test
    fun whenNoNamedaysExistOnASpecificDate_thenNoViewModelsArePassedToTheView() {
        given(mockNamedayCalendar.getAllNamedaysOn(ANY_DATE)).willReturn(NoNamesInADate(ANY_DATE))
        presenter.startPresenting(mockView, forDate = ANY_DATE)

        Mockito.verify(mockView).displayNamedays(emptyList())
    }

    @Test
    fun aNamedayWithAContact_returnsAViewModelWithThatContact() {
        given(mockSource.allContacts).willReturnContacts("Kate Brown")
        given(mockNamedayCalendar.getAllNamedaysOn(ANY_DATE)).willReturn(ImmutableNamesInADate(ANY_DATE, listOf("Kate")))

        presenter.startPresenting(mockView, forDate = ANY_DATE)

        verify(mockView).displayNamedays(
                arrayListOf(
                        NamedaysViewModel("Kate"),
                        CelebratingContactViewModel(aContactCalled("Kate Brown"), "Kate Brown", LETTER_VARIANT)
                ))
    }

    @Test
    fun aNamedayWithoutRelatedContacts_returnsOnlyTheNameday() {
        given(mockNamedayCalendar.getAllNamedaysOn(ANY_DATE)).willReturn(ImmutableNamesInADate(ANY_DATE, listOf("Kate")))

        presenter.startPresenting(mockView, forDate = ANY_DATE)

        val expectedViewModels = arrayListOf(NamedaysViewModel("Kate"))
        Mockito.verify(mockView).displayNamedays(expectedViewModels)
    }

    @Test
    fun irida() {
        given(mockSource.allContacts).willReturnContacts("Irida")
        given(mockNamedayCalendar.getAllNamedaysOn(ANY_DATE)).willReturn(ImmutableNamesInADate(ANY_DATE, listOf("Ιριδα")))

        presenter.startPresenting(mockView, forDate = ANY_DATE)

        val expectedViewModels = arrayListOf(NamedaysViewModel("Ιριδα"),
                CelebratingContactViewModel(aContactCalled("Irida"), "Irida", LETTER_VARIANT))
        Mockito.verify(mockView).displayNamedays(expectedViewModels)
    }

}
