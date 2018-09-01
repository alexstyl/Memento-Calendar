package com.alexstyl.specialdates.date

import com.alexstyl.specialdates.Optional
import com.alexstyl.specialdates.Strings
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.events.peopleevents.EventType
import com.alexstyl.specialdates.events.peopleevents.StandardEventType

data class ContactEvent(val deviceEventId: Optional<Long>, val type: EventType, val date: Date, val contact: Contact) {

    fun getLabel(dateWithYear: Date, strings: Strings): String {
        if (type === StandardEventType.BIRTHDAY && date.hasYear()) {
            val age = (dateWithYear.year!! - date.year!!).let {
                if (birthdayIsBeforeDateIgnoringYear(dateWithYear)) {
                    it + 1
                } else {
                    it
                }
            }

            if (age > 0) {
                return strings.turnsAge(age)
            }
        }
        return type.getEventName(strings)
    }

    private fun birthdayIsBeforeDateIgnoringYear(dateWithYear: Date) =
        dateWithYear.withoutYear() > date.withoutYear()
}
