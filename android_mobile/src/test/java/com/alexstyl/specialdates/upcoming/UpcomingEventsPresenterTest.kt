package com.alexstyl.specialdates.upcoming

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
    fun whenStartPresentingWithoutPermission_askForPermissions() {
        val workScheduler = Schedulers.trampoline()
        val resultScheduler = Schedulers.trampoline()

        Mockito.`when`(mockPermissions.permissionIsPresent()).thenReturn(false)
        val upcomingEventsPresenter = UpcomingEventsPresenter(mockView, mockPermissions, mockEventsMonitor, mockObserver, mockProvider, workScheduler, resultScheduler)

        upcomingEventsPresenter.startPresenting()
        Mockito.verify(mockView).askForContactPermission()
    }

    @Test
    fun startPresentingShowsLoading() {
        val workScheduler = Schedulers.trampoline()
        val resultScheduler = Schedulers.trampoline()

        Mockito.`when`(mockPermissions.permissionIsPresent()).thenReturn(true)
        val upcomingEventsPresenter = UpcomingEventsPresenter(mockView, mockPermissions, mockEventsMonitor, mockObserver, mockProvider, workScheduler, resultScheduler)

        upcomingEventsPresenter.startPresenting()
        Mockito.verify(mockView).showLoading()
    }
}
