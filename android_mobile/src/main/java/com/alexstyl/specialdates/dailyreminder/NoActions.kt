package com.alexstyl.specialdates.dailyreminder

import com.alexstyl.specialdates.person.ContactActions
import java.net.URI

class NoActions : ContactActions {
    override fun dial(phoneNumber: String) = {
        // do nothing
    }

    override fun view(data: URI, mimetype: String) = {
        // do nothing
    }

    override fun view(data: URI) = {
        // do nothing
    }

    override fun message(phoneNumber: String) = {
        // do nothing
    }

    override fun email(emailAdress: String) = {
        // do nothing
    }

}
