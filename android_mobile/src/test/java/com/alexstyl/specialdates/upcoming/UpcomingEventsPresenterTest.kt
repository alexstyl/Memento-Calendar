package com.alexstyl.specialdates.upcoming

import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.Months
import com.alexstyl.specialdates.date.TimePeriod
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsObserver
import com.alexstyl.specialdates.permissions.ContactPermissionRequest
import com.alexstyl.specialdates.settings.EventsSettingsMonitor
import com.alexstyl.specialdates.upcoming.widget.list.UpcomingEventsProvider
import io.reactivex.schedulers.Schedulers
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

    @Test
    fun whenStartPresentingWithoutPermission_askForPermission() {
        val workScheduler = Schedulers.trampoline()
        val resultScheduler = Schedulers.trampoline()

        Mockito.`when`(mockPermissions.permissionIsPresent()).thenReturn(false)
        val upcomingEventsPresenter = UpcomingEventsPresenter(mockView, mockPermissions, mockProvider, mockEventsMonitor, mockObserver, workScheduler, resultScheduler)

        upcomingEventsPresenter.startPresenting(anyDate())
        Mockito.verify(mockView).askForContactPermission()
    }

    @Test
    fun whenStartPresentingWithPermission_showLoading() {
        val workScheduler = Schedulers.trampoline()
        val resultScheduler = Schedulers.trampoline()

        Mockito.`when`(mockPermissions.permissionIsPresent()).thenReturn(true)
        val upcomingEventsPresenter = UpcomingEventsPresenter(mockView, mockPermissions, mockProvider, mockEventsMonitor, mockObserver, workScheduler, resultScheduler)

        upcomingEventsPresenter.startPresenting(anyDate())
        Mockito.verify(mockView).showLoading()
    }

    @Test
    fun whenStartPresentingWithPermission_showEventsAfterDoneLoading() {
        val workScheduler = Schedulers.trampoline()
        val resultScheduler = Schedulers.trampoline()

        val theDate = Date.on(1, Months.MARCH, 2017)
        Mockito.`when`(mockPermissions.permissionIsPresent()).thenReturn(true)
        val expectedEvents = arrayListOf<UpcomingRowViewModel>()
        Mockito.`when`(mockProvider.calculateEventsBetween(TimePeriod.aYearFrom(theDate))).thenReturn(expectedEvents)

        val upcomingEventsPresenter = UpcomingEventsPresenter(mockView, mockPermissions, mockProvider, mockEventsMonitor, mockObserver, workScheduler, resultScheduler)

        upcomingEventsPresenter.startPresenting(theDate)
        Mockito.verify(mockView).showLoading()
        Mockito.verify(mockView).display(expectedEvents)
    }

    private fun anyDate() = Mockito.any<Date>()
}
