package com.alexstyl.specialdates.addevent

import android.content.ContentProviderOperation
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Event
import android.provider.ContactsContract.CommonDataKinds.Photo
import android.provider.ContactsContract.CommonDataKinds.StructuredName
import android.provider.ContactsContract.Data
import com.alexstyl.specialdates.addevent.operations.ContactOperation
import com.alexstyl.specialdates.addevent.operations.InsertContact
import com.alexstyl.specialdates.addevent.operations.InsertEvent
import com.alexstyl.specialdates.addevent.operations.InsertImage
import com.alexstyl.specialdates.addevent.operations.UpdateContact
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.contact.ImageURL
import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.TimePeriod
import com.alexstyl.specialdates.events.database.EventTypeId
import com.alexstyl.specialdates.events.peopleevents.EventType
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsProvider
import com.alexstyl.specialdates.events.peopleevents.ShortDateLabelCreator
import com.alexstyl.specialdates.events.peopleevents.StandardEventType
import com.alexstyl.specialdates.images.ImageDecoder

class OperationsFactory(private val rawContactID: Int,
                        private val displayStringCreator: ShortDateLabelCreator,
                        private val peopleEventsProvider: PeopleEventsProvider,
                        private val accountsProvider: WriteableAccountsProvider,
                        private val imageDecoder: ImageDecoder) {

    fun createOperationsFor(contactOperation: ContactOperation): List<ContentProviderOperation> {
        when (contactOperation) {
            is InsertContact -> return createContactIn(accountToStoreContact, contactOperation.contactName)
            is UpdateContact -> return updateExistingContact(contactOperation.contact)
            is InsertEvent -> return newInsertFor(contactOperation.eventType, contactOperation.date)
            is InsertImage -> return insertImageFor(contactOperation.imageUri)
        }
        throw IllegalArgumentException("Unable to create operation for $contactOperation")
    }

    private fun insertImageFor(imageUri: ImageURL): List<ContentProviderOperation> {
        val decodeFrom = imageDecoder.decodeFrom(imageUri)
        if (decodeFrom != null) {
            val builder = ContentProviderOperation.newInsert(Data.CONTENT_URI)
                    .withValue(Data.MIMETYPE, Photo.CONTENT_ITEM_TYPE)
                    .withValue(Photo.PHOTO, decodeFrom.bytes)
            addRawContactID(builder)
            return listOf(builder.build())
        } else {
            return emptyList()
        }
    }


    private fun newInsertFor(eventType: EventType, date: Date): List<ContentProviderOperation> {
        val builder = ContentProviderOperation
                .newInsert(Data.CONTENT_URI)
                .withValue(Data.MIMETYPE, Event.CONTENT_ITEM_TYPE)
                .withValue(Event.TYPE, androidIdOf(eventType))
                .withValue(Event.START_DATE, displayStringCreator.createLabelWithYearPreferredFor(date))
        addRawContactID(builder)
        return listOf(builder.build())
    }

    private fun androidIdOf(eventType: EventType): Int = when (eventType.id) {
        EventTypeId.TYPE_BIRTHDAY -> Event.TYPE_BIRTHDAY
        EventTypeId.TYPE_ANNIVERSARY -> Event.TYPE_ANNIVERSARY
        EventTypeId.TYPE_CUSTOM -> Event.TYPE_CUSTOM
        EventTypeId.TYPE_OTHER -> Event.TYPE_OTHER
        else -> {
            throw IllegalStateException("There is no Android type of $eventType")
        }
    }

    private fun addRawContactID(builder: ContentProviderOperation.Builder) {
        if (rawContactID == NO_RAW_CONTACT_ID) {
            builder.withValueBackReference(Data.RAW_CONTACT_ID, rawContactID)
        } else {
            builder.withValue(Data.RAW_CONTACT_ID, rawContactID)

        }
    }

    private fun deleteEvents(contactEvents: List<ContactEvent>): ArrayList<ContentProviderOperation> {
        val ops = ArrayList<ContentProviderOperation>()
        for (contactEvent in contactEvents) {
            val eventId = contactEvent.deviceEventId
            ops.add(
                    ContentProviderOperation
                            .newDelete(Data.CONTENT_URI)
                            .withSelection(Event._ID + "= " + eventId, null)
                            .build())
        }
        return ops
    }

    private fun createContactIn(account: AccountData, contactName: String): ArrayList<ContentProviderOperation> {
        val ops = ArrayList<ContentProviderOperation>(2)
        ops.add(
                ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, account.accountName)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, account.accountType)
                        .build())
        ops.add(
                ContentProviderOperation.newInsert(Data.CONTENT_URI)
                        .withValueBackReference(Data.RAW_CONTACT_ID, rawContactID)
                        .withValue(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
                        .withValue(StructuredName.DISPLAY_NAME, contactName)
                        .build())

        return ops
    }


    private val accountToStoreContact: AccountData
        get() {
            val availableAccounts = accountsProvider.availableAccounts
            return if (availableAccounts.size == 0) {
                AccountData.NO_ACCOUNT
            } else {
                availableAccounts[0]
            }
        }

    private fun updateExistingContact(contact: Contact): List<ContentProviderOperation> {
        val contactEvents = getAllDeviceEventsFor(contact)
        return deleteEvents(contactEvents)
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

    companion object {

        private const val NO_RAW_CONTACT_ID = 0

        fun forNewContact(displayStringCreator: ShortDateLabelCreator,
                          peopleEventsProvider: PeopleEventsProvider,
                          accountsProvider: WriteableAccountsProvider,
                          imageDecoder: ImageDecoder): OperationsFactory {
            return OperationsFactory(NO_RAW_CONTACT_ID, displayStringCreator, peopleEventsProvider, accountsProvider,
                    imageDecoder)
        }
    }
}
