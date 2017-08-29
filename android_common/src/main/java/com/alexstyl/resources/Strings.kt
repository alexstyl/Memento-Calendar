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
}
