package com.alexstyl.specialdates.contact

import com.alexstyl.specialdates.contact.ContactSource.SOURCE_DEVICE
import java.net.URI

object ContactFixture {

    private val SOME_IMAGE = URI.create("https://www.alexstyl.com/image.jpg")

    fun aContact(): Contact {
        return Contact(-1, DisplayName.from("Test Contact"), SOME_IMAGE, SOURCE_DEVICE)
    }

    fun aContactCalled(peter: String): Contact {
        return Contact(-1, DisplayName.from(peter), SOME_IMAGE, 1)
    }

    fun with(id: Long, firstName: String): Contact {
        return Contact(id, DisplayName.from(firstName), SOME_IMAGE, SOURCE_DEVICE)
    }
}
