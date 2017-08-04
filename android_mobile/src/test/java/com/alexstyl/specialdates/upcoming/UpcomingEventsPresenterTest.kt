package com.alexstyl.specialdates.upcoming

import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.Months
import com.alexstyl.specialdates.date.TimePeriod
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsObserver
import com.alexstyl.specialdates.permissions.ContactPermissionRequest
import com.alexstyl.specialdates.settings.EventsSettingsMonitor
import com.alexstyl.specialdates.upcoming.widget.list.UpcomingEventsProvider
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.runners.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class UpcomingEventsPresenterTest {

    val mockView = Mockito.mock(UpcomingListMVPView::class.java)
    val mockPermissions = Mockito.mock(ContactPermissionRequest::class.java)
    val mockEventsMonitor = Mockito.mock(EventsSettingsMonitor::class.java)
    val mockObserver = Mockito.mock(PeopleEventsObserver::class.java)
    val mockProvider = Mockito.mock(UpcomingEventsProvider::class.java)

    private lateinit var upcomingEventsPresenter: UpcomingEventsPresenter

    @Before
    fun setUp() {
        val workScheduler = Schedulers.trampoline()
        val resultScheduler = Schedulers.trampoline()
        upcomingEventsPresenter = UpcomingEventsPresenter(mockView, mockPermissions, mockProvider, mockEventsMonitor, mockObserver, workScheduler, resultScheduler)
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

    private fun anyDate(): Date = Date.on(1, Months.APRIL, 2017)
}


