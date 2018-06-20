package com.alexstyl.specialdates.events.peopleevents

class PeopleEventsStaticEventsRefresher(
        private val peopleEventsRepository: PeopleEventsRepository,
        private val persister: PeopleEventsPersister) {

    fun rebuildEvents() {
        persister.deleteAllDeviceEvents()
        val contacts = peopleEventsRepository.fetchPeopleWithEvents()
        persister.insertAnnualEvents(contacts)
    }
}
