package com.alexstyl.specialdates.contact

import com.alexstyl.specialdates.contact.ContactSource.SOURCE_DEVICE

object ContactFixture {

    private val SOME_IMAGE = "https://www.alexstyl.com/image.jpg"

    fun aContact(): Contact {
        return Contact(-1, DisplayName.from("Test Contact"), SOME_IMAGE, SOURCE_DEVICE)
    }

    fun aContactCalled(name: String): Contact {
        return aContact().copy(displayName = DisplayName.from(name))
    }
}
