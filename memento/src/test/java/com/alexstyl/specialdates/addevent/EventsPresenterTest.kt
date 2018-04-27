package com.alexstyl.specialdates.addevent

import com.alexstyl.specialdates.JavaStrings
import com.alexstyl.specialdates.Optional
import com.alexstyl.specialdates.TestDateLabelCreator
import com.alexstyl.specialdates.contact.ContactFixture
import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsProvider
import com.alexstyl.specialdates.events.peopleevents.StandardEventType
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.mockito.Mockito.verify

class EventsPresenterTest {

    private lateinit var presenter: EventsPresenter

    private val mockView = Mockito.mock(AddEventView::class.java)
    private val mockPeopleEventsProvider = Mockito.mock(PeopleEventsProvider::class.java)
    private val factory = AddEventContactEventViewModelFactory(TestDateLabelCreator.forUS(), JavaStrings(), JavaEventIcons())

    @Before
    fun setUp() {
        presenter = EventsPresenter(
                mockPeopleEventsProvider,
                factory,
                Schedulers.trampoline(),
                Schedulers.trampoline()
        )
    }

    @Test
    fun whenStartPresenting_thenAlwaysStartsWithEmptyViewModelsForStandardTypes() {

        presenter.startPresentingInto(mockView)

        val expectedViewModels = viewModelsFor(StandardEventType.BIRTHDAY,
                StandardEventType.ANNIVERSARY,
                StandardEventType.OTHER)

        verify(mockView).display(expectedViewModels)
    }

    private fun viewModelsFor(standardEventType: StandardEventType, vararg others: StandardEventType): List<AddEventContactEventViewModel> {
        return (listOf(standardEventType) + others.toList())
                .map { factory.createAddEventViewModelFor(it) }
    }

    @Test
    fun givenAContactWithoutEvents_thenEmptyViewModelsWillBePassedToTheView() {
        presenter.startPresentingInto(mockView)

        val contact = ContactFixture.aContactCalled("Martha")
        given(mockPeopleEventsProvider.fetchEventsFor(contact)).willReturn(emptyList())

        presenter.onContactSelected(contact)

        val expectedViewModels = viewModelsFor(StandardEventType.ANNIVERSARY, StandardEventType.OTHER)
        verify(mockView).display(expectedViewModels)
    }

    @Test
    fun givenAContactWithAllEvents_thenTheViewModelsForThoseEventsArePassedIntoTheView() {
        presenter.startPresentingInto(mockView)

        val contact = ContactFixture.aContactCalled("Martha")
        val birthday = ContactEvent(Optional.absent(), StandardEventType.BIRTHDAY, Date.today(), contact)
        val anniversary = ContactEvent(Optional.absent(), StandardEventType.ANNIVERSARY, Date.today() + 1, contact)
        val other = ContactEvent(Optional.absent(), StandardEventType.OTHER, Date.today() + 2, contact)
        given(mockPeopleEventsProvider.fetchEventsFor(contact)).willReturn(listOf(birthday, anniversary, other))

        presenter.onContactSelected(contact)

        val expectedViewModels = listOf(factory.createViewModel(birthday), factory.createViewModel(anniversary), factory.createViewModel(other))
        verify(mockView).display(expectedViewModels)
    }

    @Test
    fun givenADateAndEventIsSelected_thenTheViewModelsOfThatEventPlusAllOtherEmptyViewModelsArePassedToTheView() {
        presenter.startPresentingInto(mockView)
        verify(mockView).display(viewModelsFor(StandardEventType.BIRTHDAY, StandardEventType.ANNIVERSARY, StandardEventType.OTHER))

        presenter.onEventDatePicked(StandardEventType.BIRTHDAY, Date.today())

        val dateViewModel = factory.createViewModelWith(StandardEventType.BIRTHDAY, Date.today())
        verify(mockView).display(listOf(dateViewModel) + viewModelsFor(StandardEventType.ANNIVERSARY, StandardEventType.OTHER))
    }

    @Test
    fun givenADateAndEventIsRemoved_thenTheViewModelsOfThatEventIsReturnedEmptyToTheView() {
        presenter.startPresentingInto(mockView)

        val contact = ContactFixture.aContactCalled("Martha")
        val birthday = ContactEvent(Optional.absent(), StandardEventType.BIRTHDAY, Date.today(), contact)
        val anniversary = ContactEvent(Optional.absent(), StandardEventType.ANNIVERSARY, Date.today() + 1, contact)
        val other = ContactEvent(Optional.absent(), StandardEventType.OTHER, Date.today() + 2, contact)
        given(mockPeopleEventsProvider.fetchEventsFor(contact)).willReturn(listOf(birthday, anniversary, other))

        presenter.onContactSelected(contact)

        presenter.removeEvent(StandardEventType.BIRTHDAY)

        val expectedViewModels = listOf(factory.createAddEventViewModelFor(StandardEventType.BIRTHDAY),
                factory.createViewModel(anniversary),
                factory.createViewModel(other)
        )
        verify(mockView).display(expectedViewModels)
    }
}
