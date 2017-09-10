package com.alexstyl.specialdates.addevent

import android.content.ContentProviderOperation
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds
import android.provider.ContactsContract.CommonDataKinds.Photo
import android.provider.ContactsContract.CommonDataKinds.StructuredName
import android.provider.ContactsContract.Data
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.events.Event
import com.alexstyl.specialdates.events.database.EventTypeId
import com.alexstyl.specialdates.events.peopleevents.EventType
import com.alexstyl.specialdates.events.peopleevents.ShortDateLabelCreator
import com.alexstyl.specialdates.images.DecodedImage

internal class OperationsFactory(private val rawContactID: Int, private val displayStringCreator: ShortDateLabelCreator) {

    fun newInsertFor(eventType: EventType, date: Date): ContentProviderOperation {
        val builder = ContentProviderOperation
                .newInsert(Data.CONTENT_URI)
                .withValue(Data.MIMETYPE, CommonDataKinds.Event.CONTENT_ITEM_TYPE)
                .withValue(CommonDataKinds.Event.TYPE, androidIdOf(eventType))
                .withValue(CommonDataKinds.Event.START_DATE, displayStringCreator.createLabelWithYearPreferredFor(date))
        addRawContactID(builder)
        return builder.build()
    }

    private fun androidIdOf(eventType: EventType): Int = when (eventType.id) {
        EventTypeId.TYPE_BIRTHDAY -> CommonDataKinds.Event.TYPE_BIRTHDAY
        EventTypeId.TYPE_ANNIVERSARY -> CommonDataKinds.Event.TYPE_ANNIVERSARY
        EventTypeId.TYPE_CUSTOM -> CommonDataKinds.Event.TYPE_CUSTOM
        EventTypeId.TYPE_OTHER -> CommonDataKinds.Event.TYPE_OTHER
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

    fun deleteEvents(contactEvents: List<ContactEvent>): ArrayList<ContentProviderOperation> {
        val ops = ArrayList<ContentProviderOperation>()
        for (contactEvent in contactEvents) {
            val eventId = contactEvent.deviceEventId.get()
            ops.add(
                    ContentProviderOperation
                            .newDelete(Data.CONTENT_URI)
                            .withSelection(CommonDataKinds.Event._ID + "= " + eventId, null)
                            .build())
        }
        return ops
    }

    fun createContactIn(account: AccountData, contactName: String): ArrayList<ContentProviderOperation> {
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

    fun insertEvents(events: Collection<Event>): List<ContentProviderOperation> {
        val operations = ArrayList<ContentProviderOperation>(events.size)
        for (entry in events) {
            val eventType = entry.eventType
            val date = entry.date
            val operation = newInsertFor(eventType, date)
            operations.add(operation)
        }
        return operations
    }

    fun insertImageToContact(image: DecodedImage): ContentProviderOperation {
        val builder = ContentProviderOperation.newInsert(Data.CONTENT_URI)
                .withValue(Data.MIMETYPE, Photo.CONTENT_ITEM_TYPE)
                .withValue(Photo.PHOTO, image.bytes)
        addRawContactID(builder)
        return builder.build()
    }

    fun updateImageContact(decodedImage: DecodedImage): ContentProviderOperation {
        return ContentProviderOperation
                .newUpdate(ContactsContract.Data.CONTENT_URI)
                .withSelection(
                        Data.RAW_CONTACT_ID + "= ?" + " AND " + ContactsContract.Data.MIMETYPE + "=?",
                        arrayOf(rawContactID.toString(), CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
                )
                .withValue(CommonDataKinds.Photo.PHOTO, decodedImage.bytes)
                .build()
    }

    fun deleteImageFor(contact: Contact): ContentProviderOperation {
        return ContentProviderOperation.newDelete(Data.CONTENT_URI)
                .withValueBackReference(Data.RAW_CONTACT_ID, rawContactID)
                .withValue(Data.MIMETYPE, Photo.CONTENT_ITEM_TYPE)
                .withSelection(Photo.CONTACT_ID + "=?", arrayOf(contact.contactID.toString()))
                .build()
    }

    companion object {

        private val NO_RAW_CONTACT_ID = 0

        fun forNewContact(): OperationsFactory {
            return OperationsFactory(NO_RAW_CONTACT_ID, ShortDateLabelCreator.INSTANCE)
        }
    }
}
