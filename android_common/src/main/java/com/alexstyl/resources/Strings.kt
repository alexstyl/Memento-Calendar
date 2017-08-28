package com.alexstyl.resources

import com.alexstyl.specialdates.person.StarSign

interface Strings {
    fun viewConversation(): String
    fun facebookMessenger(): String
    fun nameOf(starSign: StarSign): String
}
