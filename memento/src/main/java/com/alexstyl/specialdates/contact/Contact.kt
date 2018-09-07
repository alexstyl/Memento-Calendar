package com.alexstyl.specialdates.contact

data class Contact(val contactID: Long,
                   val displayName: DisplayName,
                   val imagePath: ImageURL,
                   @ContactSource val source: Int) {

    val givenName: String
        get() = displayName.firstNames[0]
}
