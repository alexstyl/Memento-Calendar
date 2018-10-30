package com.alexstyl.specialdates.upcoming.widget.today

import com.alexstyl.specialdates.date.ContactEvent

interface RecentPeopleEventsView {
    fun onNextDateLoaded(events: List<ContactEvent>)
    fun onNoEventsFound()
    fun askForContactPermission()
}
