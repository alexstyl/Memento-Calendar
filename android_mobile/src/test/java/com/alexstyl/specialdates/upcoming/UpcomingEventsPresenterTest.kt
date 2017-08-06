package com.alexstyl.specialdates.upcoming

import android.content.ContentResolver
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.Months
import com.alexstyl.specialdates.date.TimePeriod
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsObserver
import com.alexstyl.specialdates.permissions.ContactPermissionRequest
import com.alexstyl.specialdates.upcoming.widget.list.UpcomingEventsProvider
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.internal.verification.Times
import org.mockito.runners.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class UpcomingEventsPresenterTest {

    val mockView = Mockito.mock(UpcomingListMVPView::class.java)
    val mockPermissions = Mockito.mock(ContactPermissionRequest::class.java)
    val mockEventsMonitor = Mockito.mock(UpcomingEventsSettingsMonitor::class.java)
    val mockProvider = Mockito.mock(UpcomingEventsProvider::class.java)

    private lateinit var peopleEventsObserver: PeopleEventsObserver
    private lateinit var upcomingEventsPresenter: UpcomingEventsPresenter

    @Before
    fun setUp() {
        val workScheduler = Schedulers.trampoline()
        val resultScheduler = Schedulers.trampoline()
        peopleEventsObserver = PeopleEventsObserver(Mockito.mock(ContentResolver::class.java))
        upcomingEventsPresenter = UpcomingEventsPresenter(mockView, mockPermissions, mockProvider, mockEventsMonitor, peopleEventsObserver, workScheduler, resultScheduler)
    }

    @Test
    fun whenStartPresentingWithoutPermission_askForPermission() {
        Mockito.`when`(mockPermissions.permissionIsPresent()).thenReturn(false)

        upcomingEventsPresenter.startPresenting(anyDate())

        Mockito.verify(mockView).askForContactPermission()
    }

    @Test
    fun whenStartPresentingWithPermission_showLoading() {
        Mockito.`when`(mockPermissions.permissionIsPresent()).thenReturn(true)

        upcomingEventsPresenter.startPresenting(anyDate())

        Mockito.verify(mockView).showLoading()
    }

    @Test
    fun whenStartPresentingWithPermission_showEventsAfterDoneLoading() {
        val theDate = Date.on(1, Months.MARCH, 2017)
        val expectedEvents = arrayListOf<UpcomingRowViewModel>()
        Mockito.`when`(mockProvider.calculateEventsBetween(TimePeriod.aYearFrom(theDate))).thenReturn(expectedEvents)
        Mockito.`when`(mockPermissions.permissionIsPresent()).thenReturn(true)

        upcomingEventsPresenter.startPresenting(theDate)

        Mockito.verify(mockView).showLoading()
        Mockito.verify(mockView).display(expectedEvents)
    }

    @Test
    fun whenEventPreferencesAreUpdated_thenUpdatedEventsArePushedToTheView() {
        val theDate = Date.on(1, Months.MARCH, 2017)
        val initialEvents = arrayListOf<UpcomingRowViewModel>()
        Mockito.`when`(mockProvider.calculateEventsBetween(TimePeriod.aYearFrom(theDate))).thenReturn(initialEvents)
        Mockito.`when`(mockPermissions.permissionIsPresent()).thenReturn(true)

        upcomingEventsPresenter.startPresenting(theDate)

        val updatedEvents = arrayListOf<UpcomingRowViewModel>(YearHeaderViewModel("2017"))
        Mockito.`when`(mockProvider.calculateEventsBetween(TimePeriod.aYearFrom(theDate))).thenReturn(updatedEvents)
        peopleEventsObserver.onChange(false)

        Mockito.verify(mockView, Times(1)).display(initialEvents)
        Mockito.verify(mockView, Times(1)).display(updatedEvents)

    }

    private fun anyDate(): Date = Date.on(1, Months.APRIL, 2017)
}

