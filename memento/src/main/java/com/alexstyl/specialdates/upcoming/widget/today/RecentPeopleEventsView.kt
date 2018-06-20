package com.alexstyl.specialdates.upcoming.widget.today

import com.alexstyl.specialdates.events.peopleevents.ContactEventsOnADate

interface RecentPeopleEventsView {
    fun onNextDateLoaded(events: ContactEventsOnADate)
    fun onNoEventsFound()
    fun askForContactPermission()
}
