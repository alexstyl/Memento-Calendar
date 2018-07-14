package com.alexstyl.specialdates.facebook.friendimport

import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsPersister

import com.alexstyl.specialdates.contact.ContactSource.SOURCE_FACEBOOK

class FacebookFriendsPersister(private val persister: PeopleEventsPersister) {

    fun storeFriends(friends: List<ContactEvent>) {
        persister.deleteAllEventsOfSource(SOURCE_FACEBOOK)
        persister.insertAnnualEvents(friends)
    }

    fun removeAllFriends() {
        persister.deleteAllEventsOfSource(SOURCE_FACEBOOK)
    }
}
