package com.alexstyl.specialdates

import com.alexstyl.specialdates.events.namedays.NamedayLocale
import com.alexstyl.specialdates.events.peopleevents.EventType
import com.alexstyl.specialdates.person.StarSign

interface Strings {
    fun viewConversation(): String
    fun dailyReminder(): String
    fun facebookMessenger(): String
    fun nameOf(starSign: StarSign): String
    fun turnsAge(age: Int): String
    fun inviteFriend(): String
    fun todaysNamedays(numberOfNamedays: Int): String
    fun donateAmount(amount: String): String
    fun eventOnDate(eventLabel: String, dateLabel: String): String
    fun appName(): String
    fun shareText(): String
    fun today(): String
    fun tomorrow(): String
    fun todayCelebrateTwo(nameOne: String, nameTwo: String): String
    fun todayCelebrateMany(name: String, numberLeft: Int): String
    fun nameOfEvent(event: EventType): String
    fun postOnFacebook(): String
    fun facebook(): String
    fun localeName(locale: NamedayLocale): String
    fun viewFacebookProfile(): String
    fun importFromFacebook(): String
    fun namedays(): String
    fun bankholidays(): String
    fun contacts(): String
    fun bankholidaySubtitle(): String
    fun call(): String
    fun sendWishes(): String
    fun dontForgetToSendWishes(): String
}
