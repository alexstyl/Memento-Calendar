package com.alexstyl.specialdates.addevent

import android.content.ContentProviderOperation
import android.content.ContentResolver
import android.database.Cursor
import android.provider.ContactsContract
import com.alexstyl.specialdates.addevent.ContactOperations.ContactOperationsBuilder

import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.date.TimePeriod
import com.alexstyl.specialdates.events.Event
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsProvider
import com.alexstyl.specialdates.events.peopleevents.ShortDateLabelCreator
import com.alexstyl.specialdates.events.peopleevents.StandardEventType
import com.novoda.notils.exception.DeveloperError
import java.net.URI

import java.util.ArrayList

class AndroidContactOperations(private val contentResolver: ContentResolver,
                               private val accountsProvider: WriteableAccountsProvider,
                               private val peopleEventsProvider: PeopleEventsProvider,
                               private val displayStringCreator: ShortDateLabelCreator) : ContactOperations {

    private val accountToStoreContact: AccountData
        get() {
            val availableAccounts = accountsProvider.availableAccounts
            return if (availableAccounts.size == 0) {
                AccountData.NO_ACCOUNT
            } else {
                availableAccounts[0]
            }
        }

    override fun updateExistingContact(contact: Contact): ContactOperationsBuilder {
        val rawContactID = rawContactID(contact)
        val operationsFactory = OperationsFactory(rawContactID, displayStringCreator)
        val contactEvents = getAllDeviceEventsFor(contact)
        val operations = operationsFactory.deleteEvents(contactEvents)
        return AndroidContactOperationsBuilder(operations, operationsFactory)
    }

    override fun createNewContact(contactName: String): ContactOperationsBuilder {
        val operationsFactory = OperationsFactory.forNewContact(displayStringCreator)
        val operations = operationsFactory.createContactIn(accountToStoreContact, contactName)
        return AndroidContactOperationsBuilder(operations, operationsFactory)
    }

    class AndroidContactOperationsBuilder(private val existingOperations: ArrayList<ContentProviderOperation>,
                                          private val operationsFactory: OperationsFactory) : ContactOperationsBuilder {
        private var events: Collection<Event>? = null

        override fun withEvents(events: Collection<Event>): ContactOperationsBuilder {
            this.events = events
            return this
        }

        override fun addContactImage(decodedImage: URI): ContactOperationsBuilder {
//            existingOperations.add(operationsFactory.insertImageToContact(decodedImage))
            return this
        }

        override fun updateContactImage(decodedImage: URI): ContactOperationsBuilder {
//            existingOperations.add(operationsFactory.updateImageContact(decodedImage))
            return this
        }

        override fun build(): List<Any> {
            val operations = ArrayList(existingOperations)
//            if (events!!.size > 0) {
//                operations.addAll(operationsFactory.insertEvents(events!!))
//            }
//            return operations
            return emptyList<Any>()
        }
    }

    private fun rawContactID(contact: Contact): Int {
        val projection = arrayOf(ContactsContract.CommonDataKinds.Event.RAW_CONTACT_ID)
        val selection = ContactsContract.CommonDataKinds.Event.CONTACT_ID + " = ?"
        val selectionArgs = arrayOf(contact.contactID.toString())
        val cursor = contentResolver.query(ContactsContract.Data.CONTENT_URI, projection, selection, selectionArgs, null)

        throwIfInvalid(cursor)

        try {
            if (cursor!!.moveToFirst()) {
                return cursor.getInt(0)
            }
        } finally {
            cursor!!.close()
        }
        return 0
    }

    private fun getAllDeviceEventsFor(contact: Contact): List<ContactEvent> {
        val contactEvents = ArrayList<ContactEvent>()
        val contactEventsOnDate = peopleEventsProvider.fetchEventsBetween(TimePeriod.aYearFromNow())
        for (contactEvent in contactEventsOnDate) {
            val (contactID) = contactEvent.contact
            if (contactID == contact.contactID && contactEvent.type !== StandardEventType.NAMEDAY) {
                contactEvents.add(contactEvent)
            }
        }
        return contactEvents
    }

    private fun throwIfInvalid(cursor: Cursor?) {
        if (cursor == null || cursor.isClosed) {
            throw DeveloperError("Cursor was invalid")
        }
    }
}
