package com.alexstyl.specialdates.facebook.friendimport

import com.alexstyl.specialdates.Optional
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.contact.ContactSource.SOURCE_FACEBOOK
import com.alexstyl.specialdates.contact.DisplayName
import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.DateParseException
import com.alexstyl.specialdates.date.DateParser
import com.alexstyl.specialdates.events.peopleevents.StandardEventType
import com.alexstyl.specialdates.facebook.FacebookImagePath

class FacebookContactFactory(private val parser: DateParser) {

    @Throws(InvalidFacebookContactException::class)
    fun createContactFrom(map: Map<String, String>): ContactEvent {
        try {
            val date = dateFrom(map)
            val name = nameFrom(map)
            val uid = idOf(map)
            val imagePath = FacebookImagePath.forUid(uid)
            return ContactEvent(Optional.absent(), StandardEventType.BIRTHDAY, date, Contact(uid, name, imagePath, SOURCE_FACEBOOK))
        } catch (ex: DateParseException) {
            throw InvalidFacebookContactException(ex)
        } catch (ex: IndexOutOfBoundsException) {
            throw InvalidFacebookContactException(ex)
        }

    }

    @Throws(DateParseException::class)
    private fun dateFrom(map: Map<String, String>): Date {
        val dateString = getOrThrow(map, "DTSTART")
        return parser.parseWithoutYear(dateString)
    }

    private fun nameFrom(map: Map<String, String>): DisplayName {
        val summary = getOrThrow(map, "SUMMARY")
        val endOfName = summary.indexOf("'s birthday")
        return DisplayName.from(summary.substring(0, endOfName))
    }

    private fun idOf(map: Map<String, String>): Long {
        val uid = getOrThrow(map, "UID")
        val facebookMail = uid.indexOf("@facebook.com")
        return java.lang.Long.parseLong(uid.substring(1, facebookMail))
    }

    private fun getOrThrow(map: Map<String, String>, key: String): String {
        if (map.containsKey(key)) {
            return map[key]!!
        }
        throw IllegalArgumentException("Map did not contain $key")
    }
}
