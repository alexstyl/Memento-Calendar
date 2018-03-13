package com.alexstyl.specialdates.events.namedays

import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsPersister
import com.alexstyl.specialdates.events.peopleevents.PeopleDynamicNamedaysProvider

class NamedayDatabaseRefresher(private val namedayUserSettings: NamedayUserSettings,
                               private val perister: PeopleEventsPersister,
                               private val provider: PeopleDynamicNamedaysProvider) {

    fun refreshNamedaysIfEnabled() {
        perister.deleteAllNamedays()
        if (namedayUserSettings.isEnabled) {
            initialiseNamedays()
        }
    }

    private fun initialiseNamedays() {
        val namedays = provider.loadAllStaticNamedays()
        storeNamedaysToDisk(namedays)
    }

    private fun storeNamedaysToDisk(namedays: List<ContactEvent>) {
        perister.insertAnnualEvents(namedays)
    }

}
