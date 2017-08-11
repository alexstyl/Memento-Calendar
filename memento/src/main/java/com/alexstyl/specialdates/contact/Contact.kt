package com.alexstyl.specialdates.contact

import java.net.URI

data class Contact(val contactID: Long, val displayName: DisplayName, val imagePath: URI, @ContactSource val source: Int) {

    override fun toString(): String {
        return displayName.toString()
    }

    val givenName: String
        get() = displayName.firstNames.primary
}
