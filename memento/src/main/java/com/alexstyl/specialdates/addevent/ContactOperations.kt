package com.alexstyl.specialdates.addevent

import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.events.Event
import java.net.URI

interface ContactOperations {
    fun updateExistingContact(contact: Contact): ContactOperationsBuilder
    fun createNewContact(contactName: String): ContactOperationsBuilder


    interface ContactOperationsBuilder {
        fun withEvents(events: Collection<Event>): ContactOperationsBuilder
        fun addContactImage(decodedImage: URI): ContactOperationsBuilder
        fun updateContactImage(decodedImage: URI): ContactOperationsBuilder
        fun build(): List<Any>
    }
}



