package com.alexstyl.resources

import com.alexstyl.specialdates.Strings
import com.alexstyl.specialdates.events.namedays.NamedayLocale
import com.alexstyl.specialdates.events.peopleevents.EventType
import com.alexstyl.specialdates.events.peopleevents.StandardEventType
import com.alexstyl.specialdates.person.StarSign

class JavaStrings : Strings {
    override fun contactUpdated(): String = "contactUpdated"

    override fun contactAdded(): String = "contactAdded"

    override fun contactAddedFailed(): String = "contactAddedFailed"

    override fun contactUpdateFailed(): String = "contactUpdateFailed"

    override fun dontForgetToSendWishes(): String = "Don't forget to send your wishes!"
    override fun call(): String = "Call"

    override fun sendWishes(): String = "Send wishes"

    override fun bankholidaySubtitle(): String = "Tap to see more events."
    override fun contacts(): String = "Contacts"
    override fun bankholidays(): String = "Bank Holidays"

    override fun namedays(): String = "Namedays"

    override fun dailyReminder(): String = "Daily Reminder"
    override fun importFromFacebook(): String = "Import from Facebook"

    override fun viewFacebookProfile(): String = "View Facebook Friends"

    override fun postOnFacebook(): String = "Post on Facebook"

    override fun facebook(): String = "Facebook"

    override fun viewConversation(): String = "View Conversations"

    override fun facebookMessenger(): String = "Messenger"

    override fun nameOf(starSign: StarSign): String = when (starSign) {
        StarSign.AQUARIUS -> "Aquarius"
        StarSign.PISCES -> "Pisces"
        StarSign.ARIES -> "Aries"
        StarSign.TAURUS -> "Taurus"
        StarSign.GEMINI -> "Gemini"
        StarSign.CANCER -> "Cancer"
        StarSign.LEO -> "Leo"
        StarSign.VIRGO -> "Virgo"
        StarSign.LIBRA -> "Libra"
        StarSign.SCORPIO -> "Scorpio"
        StarSign.SAGITTARIUS -> "Sagittarius"
        StarSign.CAPRICORN -> "Capricorn"
    }

    override fun turnsAge(age: Int): String = "Turns " + age

    override fun inviteFriend(): String = "Invite Friend"

    override fun todaysNamedays(numberOfNamedays: Int): String {
        return if (numberOfNamedays > 0) {
            "todays namedays"
        } else {
            "todays nameday"
        }
    }

    override fun donateAmount(amount: String): String = "Donate " + amount

    override fun eventOnDate(eventLabel: String, dateLabel: String): String = eventLabel + " on " + dateLabel

    override fun appName(): String = "DEBUG APP"

    override fun shareText(): String = "Share Text"

    override fun today(): String = "today"

    override fun tomorrow(): String = "tomorrow"

    override fun todayCelebrateTwo(nameOne: String, nameTwo: String): String = "Today celebrate $nameOne and $nameTwo"

    override fun todayCelebrateMany(name: String, numberLeft: Int): String = "Today celebrate $name and $numberLeft other"

    override fun nameOfEvent(event: EventType): String = when (event) {
        StandardEventType.BIRTHDAY -> "Birthday"
        StandardEventType.NAMEDAY -> "Nameday"
        StandardEventType.ANNIVERSARY -> "Anniversary"
        StandardEventType.OTHER -> "Other"
        StandardEventType.CUSTOM -> "Custom"
        else -> {
            throw IllegalStateException("$event has no name")
        }
    }

    override fun localeName(locale: NamedayLocale): String = when (locale) {
        NamedayLocale.GREEK -> "Greek"
        NamedayLocale.ROMANIAN -> "Romanian"
        NamedayLocale.RUSSIAN -> "Russian"
        NamedayLocale.LATVIAN -> "Latvian"
        NamedayLocale.LATVIAN_EXTENDED -> "Latvian (Extended)"
        NamedayLocale.SLOVAK -> "Slovak"
        NamedayLocale.ITALIAN -> "Italian"
        NamedayLocale.CZECH -> "Czech"
        NamedayLocale.HUNGARIAN -> "Hungarian"
    }
}
