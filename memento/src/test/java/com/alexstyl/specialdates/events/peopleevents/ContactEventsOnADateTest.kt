package com.alexstyl.specialdates.events.peopleevents

import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.Months
import org.junit.Test

class ContactEventsOnADateTest {

    @Test(expected = UnsupportedOperationException::class)
    fun whenAddingTwoEvents_givenTheDateAreDifferent_thenThrowsException() {
        eventOn(Date.on(13, Months.JANUARY, 1922)) +
                eventOn(Date.on(1, Months.APRIL, 1922))
    }

    private fun eventOn(date: Date) = ContactEventsOnADate.createFrom(date, emptyList())
}
