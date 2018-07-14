package com.alexstyl.specialdates.facebook.friendimport

import com.alexstyl.specialdates.events.peopleevents.UpcomingEventsViewRefresher
import com.alexstyl.specialdates.facebook.FacebookUserSettings
import com.alexstyl.specialdates.facebook.UserCredentials
import io.reactivex.Observable
import io.reactivex.Scheduler


class FacebookFriendsUpdater(
        private val facebookUserSettings: FacebookUserSettings,
        private val calendarFetcher: FacebookBirthdaysProvider,
        private val facebookFriendsPersister: FacebookFriendsPersister,
        private val calendarURLCreator: CalendarURLCreator,
        private val viewRefresher: UpcomingEventsViewRefresher,
        private val workScheduler: Scheduler,
        private val resultScheduler: Scheduler) {

    fun fetchFriends() = Observable.fromCallable {
        val userCredentials = facebookUserSettings.retrieveCredentials()
        if (isAnnonymous(userCredentials)) {
            throw FacebookUserLoggedOutException()
        }
        val calendarUrl = calendarURLCreator.createFrom(userCredentials)
        val friends = calendarFetcher.fetchCalendarFrom(calendarUrl)
        facebookFriendsPersister.storeFriends(friends)
    }.map {
        viewRefresher.refreshViews()
    }
            .subscribeOn(workScheduler)
            .observeOn(resultScheduler)

    private fun isAnnonymous(userCredentials: UserCredentials): Boolean {
        return UserCredentials.ANONYMOUS == userCredentials
    }

}
