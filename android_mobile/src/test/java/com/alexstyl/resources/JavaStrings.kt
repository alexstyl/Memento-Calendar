package com.alexstyl.resources

import com.alexstyl.specialdates.person.StarSign

class JavaStrings : Strings {

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

    override fun nameOfEvent(eventResId: Int): String = "EVENT"
}
