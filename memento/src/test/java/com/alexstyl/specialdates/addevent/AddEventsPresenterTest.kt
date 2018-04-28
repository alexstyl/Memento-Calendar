package com.alexstyl.specialdates.addevent

import com.alexstyl.specialdates.JavaStrings
import com.alexstyl.specialdates.Optional
import com.alexstyl.specialdates.TestDateLabelCreator
import com.alexstyl.specialdates.addevent.operations.InsertContact
import com.alexstyl.specialdates.addevent.operations.InsertEvent
import com.alexstyl.specialdates.addevent.operations.UpdateContact
import com.alexstyl.specialdates.analytics.Analytics
import com.alexstyl.specialdates.contact.ContactFixture
import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.Months
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsProvider
import com.alexstyl.specialdates.events.peopleevents.StandardEventType
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify

class AddEventsPresenterTest {
    private lateinit var presenter: AddEventsPresenter
    private val strings = JavaStrings()
    private val viewModelFactory = AddEventViewModelFactory(TestDateLabelCreator.forUS(), JavaStrings(), JavaEventIcons())

    private val mockView = Mockito.mock(AddEventView::class.java)
    private val mockMessageDisplayer = mock(MessageDisplayer::class.java)
    private val mockPeopleEventsProvider = Mockito.mock(PeopleEventsProvider::class.java)
    private val mockExecutor = mock(ContactOperationsExecutor::class.java)

    @Before
    fun setUp() {
        presenter = AddEventsPresenter(
                mock(Analytics::class.java),
                ContactOperations(),
                mockMessageDisplayer,
                mockExecutor,
                strings,
                mockPeopleEventsProvider,
                viewModelFactory,
                Schedulers.trampoline(),
                Schedulers.trampoline()
        )
    }

    @Test
    fun whenStartPresenting_thenAlwaysStartsWithEmptyViewModelsForStandardTypes() {
        presenter.startPresentingInto(mockView)

        val expectedViewModels = emptyModelsFor(StandardEventType.BIRTHDAY,
                StandardEventType.ANNIVERSARY,
                StandardEventType.OTHER)

        Mockito.verify(mockView).display(expectedViewModels)
    }

    private fun emptyModelsFor(standardEventType: StandardEventType, vararg others: StandardEventType): List<AddEventContactEventViewModel> {
        return (listOf(standardEventType) + others.toList())
                .map { viewModelFactory.createViewModelFor(it) }
    }

    @Test
    fun givenAContactWithoutEvents_thenEmptyViewModelsWillBePassedToTheView() {
        presenter.startPresentingInto(mockView)

        val contact = ContactFixture.aContactCalled("Martha")
        BDDMockito.given(mockPeopleEventsProvider.fetchEventsFor(contact)).willReturn(emptyList())

        presenter.presentContact(contact)

        Mockito.verify(mockView, times(2)).display(emptyModelsFor(StandardEventType.BIRTHDAY, StandardEventType.ANNIVERSARY, StandardEventType.OTHER))
    }

    @Test
    fun givenAContactWithAllEvents_thenTheViewModelsForThoseEventsArePassedIntoTheView() {
        presenter.startPresentingInto(mockView)

        val contact = ContactFixture.aContactCalled("Martha")
        val birthday = ContactEvent(Optional.absent(), StandardEventType.BIRTHDAY, Date.today(), contact)
        val anniversary = ContactEvent(Optional.absent(), StandardEventType.ANNIVERSARY, Date.today() + 1, contact)
        val other = ContactEvent(Optional.absent(), StandardEventType.OTHER, Date.today() + 2, contact)
        BDDMockito.given(mockPeopleEventsProvider.fetchEventsFor(contact)).willReturn(listOf(birthday, anniversary, other))

        presenter.presentContact(contact)

        val expectedViewModels = listOf(viewModelFactory.createViewModelFor(birthday), viewModelFactory.createViewModelFor(anniversary), viewModelFactory.createViewModelFor(other))
        Mockito.verify(mockView).display(expectedViewModels)
    }

    @Test
    fun givenAContactWithCustomEvents_thenTheViewModelsForThoseEventsButNoCustomArePassedIntoTheView() {
        presenter.startPresentingInto(mockView)

        val contact = ContactFixture.aContactCalled("Martha")
        val nameday = ContactEvent(Optional.absent(), StandardEventType.NAMEDAY, Date.today() + 2, contact)
        val custom = ContactEvent(Optional.absent(), StandardEventType.CUSTOM, Date.today() + 2, contact)

        BDDMockito.given(mockPeopleEventsProvider.fetchEventsFor(contact)).willReturn(listOf(nameday, custom))

        presenter.presentContact(contact)

        val expectedViewModels = emptyModelsFor(StandardEventType.BIRTHDAY, StandardEventType.ANNIVERSARY, StandardEventType.OTHER)
        Mockito.verify(mockView, times(2)).display(expectedViewModels)
    }

    @Test
    fun givenADateAndEventIsSelected_thenTheViewModelsOfThatEventPlusAllOtherEmptyViewModelsArePassedToTheView() {
        presenter.startPresentingInto(mockView)
        Mockito.verify(mockView).display(emptyModelsFor(StandardEventType.BIRTHDAY, StandardEventType.ANNIVERSARY, StandardEventType.OTHER))

        presenter.onEventDatePicked(StandardEventType.BIRTHDAY, Date.today())

        val dateViewModel = viewModelFactory.createViewModelFor(StandardEventType.BIRTHDAY, Date.today())
        Mockito.verify(mockView).display(listOf(dateViewModel) + emptyModelsFor(StandardEventType.ANNIVERSARY, StandardEventType.OTHER))
    }

    @Test
    fun givenADateAndEventIsRemoved_thenTheViewModelsOfThatEventIsReturnedEmptyToTheView() {
        presenter.startPresentingInto(mockView)

        val contact = ContactFixture.aContactCalled("Martha")
        val birthday = ContactEvent(Optional.absent(), StandardEventType.BIRTHDAY, Date.today(), contact)
        val anniversary = ContactEvent(Optional.absent(), StandardEventType.ANNIVERSARY, Date.today() + 1, contact)
        val other = ContactEvent(Optional.absent(), StandardEventType.OTHER, Date.today() + 2, contact)
        BDDMockito.given(mockPeopleEventsProvider.fetchEventsFor(contact)).willReturn(listOf(birthday, anniversary, other))

        presenter.presentContact(contact)

        presenter.removeEvent(StandardEventType.BIRTHDAY)

        val expectedViewModels = listOf(viewModelFactory.createViewModelFor(StandardEventType.BIRTHDAY),
                viewModelFactory.createViewModelFor(anniversary),
                viewModelFactory.createViewModelFor(other)
        )
        Mockito.verify(mockView).display(expectedViewModels)
    }


    @Test
    fun whenStartPresenting_thenSaveIsDisabled() {
        presenter.startPresentingInto(mockView)

        verify(mockView).preventSave()
    }

    @Test
    fun whenAContactIsSelected_thenSaveIsDisabled() {
        presenter.startPresentingInto(mockView)

        val contact = ContactFixture.aContactCalled("Rob")
        presenter.presentContact(contact)

        verify(mockView, times(2)).preventSave()
        verify(mockView, times(0)).allowSave()
    }

    @Test(expected = UnsupportedOperationException::class)
    fun givenAName_whenAContactIsSelected_thenThrowsException() {
        presenter.startPresentingInto(mockView)
        presenter.presentContact(ContactFixture.aContactCalled("Yoland"))
        presenter.presentName("Yolanda")
    }

    @Test
    fun whenAnEventIsSelected_givenAContactIsSelected_thenSaveIsEnabled() {
        presenter.startPresentingInto(mockView)

        presenter.presentName("Alex")

        verify(mockView, times(2)).preventSave()
        verify(mockView, times(0)).allowSave()

        presenter.onEventDatePicked(StandardEventType.BIRTHDAY, Date.on(19, Months.DECEMBER, 1990))

        verify(mockView, times(1)).allowSave()
    }

    @Test
    fun whenAllEventsAreRemoved_givenAContact_thenSaveIsDisabled() {
        presenter.startPresentingInto(mockView) // prevent save 1

        val contact = ContactFixture.aContactCalled("Chrysa")
        given(mockPeopleEventsProvider.fetchEventsFor(contact))
                .willReturn(listOf(
                        ContactEvent(Optional.absent(), StandardEventType.BIRTHDAY, Date.today(), contact))
                )

        presenter.presentContact(contact)    // prevent save 2
        presenter.removeEvent(StandardEventType.BIRTHDAY)     // prevent save 3

        verify(mockView, times(3)).preventSave()
    }


    @Test
    fun givenANameABirthday_thenAContactIsCreatedMessageIsShown() {
        given(mockExecutor.execute(
                listOf(InsertContact("Alex"), InsertEvent(StandardEventType.BIRTHDAY, Date.on(19, Months.DECEMBER, 1990))))
        ).willReturn(true)

        presenter.startPresentingInto(mockView)
        presenter.presentName("Alex")
        presenter.onEventDatePicked(StandardEventType.BIRTHDAY, Date.on(19, Months.DECEMBER, 1990))
        presenter.saveChanges()

        verify(mockMessageDisplayer).showMessage(strings.contactAdded())
    }

    @Test
    fun givenANameABirthday_thenANewContactIsCreated() {
        presenter.startPresentingInto(mockView)
        presenter.presentName("Alex")
        presenter.onEventDatePicked(StandardEventType.BIRTHDAY, Date.on(19, Months.DECEMBER, 1990))
        presenter.saveChanges()

        verify(mockExecutor).execute(
                listOf(
                        InsertContact("Alex"),
                        InsertEvent(StandardEventType.BIRTHDAY, Date.on(19, Months.DECEMBER, 1990))
                ))
    }

    @Test
    fun givenAContact_whenTheContactHasAnExistingBirthday_thenTheBirthdayWillBeUpdated() {
        presenter.startPresentingInto(mockView)
        val selectedContact = ContactFixture.aContactCalled("Joseph")
        presenter.presentContact(selectedContact)

        val existingBirthday = ContactEvent(Optional.absent(), StandardEventType.BIRTHDAY, Date.on(1, Months.JANUARY), selectedContact)

        given(mockPeopleEventsProvider.fetchEventsFor(selectedContact)).willReturn(listOf(existingBirthday))

        presenter.onEventDatePicked(StandardEventType.BIRTHDAY, Date.on(19, Months.DECEMBER, 1990))
        presenter.saveChanges()

        verify(mockExecutor).execute(
                listOf(
                        UpdateContact(selectedContact),
                        InsertEvent(StandardEventType.BIRTHDAY, Date.on(19, Months.DECEMBER, 1990))
                ))
    }

//    @Test
//    fun givenAView_whenNoUpdatesArePassed_thenNothingHappens() {
//        presenter.startPresentingInto(mockView)
//
//        presenter.saveChanges()
//
//        verifyNoMoreInteractions(mockExecutor)
//    }

}
