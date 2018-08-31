package com.alexstyl.specialdates.addevent

import com.alexstyl.specialdates.addevent.operations.ContactOperation
import com.alexstyl.specialdates.addevent.operations.InsertContact
import com.alexstyl.specialdates.addevent.operations.InsertEvent
import com.alexstyl.specialdates.addevent.operations.InsertImage
import com.alexstyl.specialdates.addevent.operations.UpdateContact
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.contact.ImageURL
import com.alexstyl.specialdates.events.Event

class ContactOperations {

    fun updateExistingContact(contact: Contact): ContactOperationsBuilder {
        return ContactOperationsBuilder(arrayListOf(UpdateContact(contact)))
    }

    fun newContact(contactName: String): ContactOperationsBuilder {
        return ContactOperationsBuilder(arrayListOf(InsertContact(contactName)))
    }

    class ContactOperationsBuilder(private val existingOperations: List<ContactOperation>) {
        private var events: List<Event>? = null
        private var imageUri: ImageURL? = null

        fun withEvents(events: List<Event>): ContactOperationsBuilder {
            this.events = events
            return this
        }

        fun withImage(value: ImageURL): ContactOperationsBuilder {
            this.imageUri = value
            return this
        }

        fun build(): List<ContactOperation> {
            return existingOperations +
                    (events?.map { InsertEvent(it.eventType, it.date) }
                            ?: emptyList()) + operationFor(imageUri)
        }

        private fun operationFor(imageUri: ImageURL?): List<ContactOperation> {
            return if (imageUri == null || imageUri.isEmpty()) {
                emptyList()
            } else {
                listOf(InsertImage(imageUri))
            }
        }
    }
}
