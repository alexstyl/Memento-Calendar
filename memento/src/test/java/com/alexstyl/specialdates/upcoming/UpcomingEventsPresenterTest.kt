package com.alexstyl.specialdates.upcoming

import com.alexstyl.specialdates.date.Months
import com.alexstyl.specialdates.date.TimePeriod
import com.alexstyl.specialdates.date.dateOn
import com.alexstyl.specialdates.events.peopleevents.UpcomingEventsSettings
import com.alexstyl.specialdates.permissions.MementoPermissions
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.mockito.internal.verification.Times
import org.mockito.runners.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class UpcomingEventsPresenterTest {

    companion object {
        private val STARTING_DATE = dateOn(1, Months.APRIL, 2017)
    }

    private val mockView = Mockito.mock(UpcomingListMVPView::class.java)
    private val mockPermissions = Mockito.mock(MementoPermissions::class.java)
    private val mockProvider = Mockito.mock(UpcomingEventsProvider::class.java)
    private val mockSettings = Mockito.mock(UpcomingEventsSettings::class.java)

    private lateinit var upcomingEventsPresenter: UpcomingEventsPresenter

    @Before
    fun setUp() {
        upcomingEventsPresenter = UpcomingEventsPresenter(
                STARTING_DATE,
                mockPermissions,
                mockProvider,
                Schedulers.trampoline(),
                Schedulers.trampoline()
        )
        given(mockSettings.hasBeenInitialised()).willReturn(true)
        given(mockView.isShowingNoEvents).willReturn(true) // start each test with from scratch
        given(mockPermissions.canReadAndWriteContacts()).willReturn(true)
    }

    @Test
    fun whenStartPresentingWithoutPermission_showsLoading() {
        given(mockPermissions.canReadAndWriteContacts()).willReturn(false)

        upcomingEventsPresenter.startPresentingInto(mockView)

        Mockito.verify(mockView).showLoading()
    }

    @Test
    fun whenStartPresentingWithPermission_showLoading() {
        given(mockPermissions.canReadAndWriteContacts()).willReturn(true)

        upcomingEventsPresenter.startPresentingInto(mockView)

        Mockito.verify(mockView).showLoading()
    }

    @Test
    fun whenStartPresentingWithPermission_showEventsAfterDoneLoading() {
        val theDate = dateOn(1, Months.MARCH, 2017)
        val expectedEvents = arrayListOf<UpcomingRowViewModel>()
        given(mockProvider.calculateEventsBetween(TimePeriod.aYearFrom(theDate))).willReturn(expectedEvents)

        upcomingEventsPresenter.startPresentingInto(mockView)

        Mockito.verify(mockView).showLoading()
        Mockito.verify(mockView).display(expectedEvents)
    }

    @Test
    fun givenEventsWereUpdated_whenAskedToUpdate_thenNewEventsArePushedToTheView() {
        val initialEvents = arrayListOf<UpcomingRowViewModel>()
        given(mockProvider.calculateEventsBetween(TimePeriod.aYearFrom(STARTING_DATE))).willReturn(initialEvents)

        upcomingEventsPresenter.startPresentingInto(mockView)

        val updatedEvents = arrayListOf<UpcomingRowViewModel>(DateHeaderViewModel("February 2017", 2))
        given(mockProvider.calculateEventsBetween(TimePeriod.aYearFrom(STARTING_DATE))).willReturn(updatedEvents)
        upcomingEventsPresenter.refreshEvents()

        Mockito.verify(mockView, Times(1)).display(initialEvents)
        Mockito.verify(mockView, Times(1)).display(updatedEvents)
    }
}


