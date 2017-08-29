package com.alexstyl.resources

import com.alexstyl.specialdates.person.StarSign

interface Strings {
    fun viewConversation(): String
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
}
