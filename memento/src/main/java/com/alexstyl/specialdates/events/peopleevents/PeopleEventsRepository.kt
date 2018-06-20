package com.alexstyl.specialdates.events.peopleevents

import com.alexstyl.specialdates.date.ContactEvent

interface PeopleEventsRepository {
    fun fetchPeopleWithEvents(): List<ContactEvent>
}
