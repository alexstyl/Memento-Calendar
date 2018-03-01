package com.alexstyl.specialdates.events.peopleevents

import com.alexstyl.specialdates.date.ContactEvent

class PeopleEventsStaticEventsRefresher(
        private val repositoryPeople: PeopleEventsRepository,
        private val persister: PeopleEventsPersister) {

    fun rebuildEvents() {
        persister.deleteAllDeviceEvents()
        val contacts = repositoryPeople.fetchPeopleWithEvents()
        storeContactsToProvider(contacts)
    }

    private fun storeContactsToProvider(contacts: List<ContactEvent>) {
        persister.insertAnnualEvents(contacts)
    }

}
