package com.alexstyl.specialdates.events.namedays

import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsPersister
import com.alexstyl.specialdates.events.peopleevents.PeopleNamedaysCalculator

class NamedayDatabaseRefresher(private val namedayUserSettings: NamedayUserSettings,
                               private val perister: PeopleEventsPersister,
                               private val calculator: PeopleNamedaysCalculator) {

    fun refreshNamedaysIfEnabled() {
        perister.deleteAllNamedays()
        if (namedayUserSettings.isEnabled) {
            initialiseNamedays()
        }
    }

    private fun initialiseNamedays() {
        val namedays = calculator.loadDeviceStaticNamedays()
        storeNamedaysToDisk(namedays)
    }

    private fun storeNamedaysToDisk(namedays: List<ContactEvent>) {
        perister.insertAnnualEvents(namedays)
    }

}
