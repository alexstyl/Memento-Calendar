package com.alexstyl.specialdates.events.peopleevents

import android.content.ContentResolver
import android.database.Cursor
import android.provider.ContactsContract
import com.alexstyl.specialdates.CrashAndErrorTracker
import com.alexstyl.specialdates.contact.ContactNotFoundException
import com.alexstyl.specialdates.contact.ContactSource.SOURCE_DEVICE
import com.alexstyl.specialdates.contact.ContactsProvider
import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.DateParseException
import com.alexstyl.specialdates.date.DateParser
import com.novoda.notils.logger.simple.Log

class AndroidPeopleEventsRepository(private val contentResolver: ContentResolver,
                                    private val contactsProvider: ContactsProvider,
                                    private val dateParser: DateParser,
                                    private val tracker: CrashAndErrorTracker) : PeopleEventsRepository {

    override fun fetchPeopleWithEvents(): List<ContactEvent> {
        val cursor = contentResolver.query(CONTENT_URI, PROJECTION, SELECTION, SELECT_ARGS, SORT_ORDER)
        if (isInvalid(cursor)) {
            return emptyList()
        }
        val events = ArrayList<ContactEvent>()
        try {
            while (cursor!!.moveToNext()) {
                val contactId = getContactIdFrom(cursor)
                val eventType = getEventTypeFrom(cursor)
                try {
                    val eventDate = getEventDateFrom(cursor)
                    val eventId = getEventIdFrom(cursor)
                    val contact = contactsProvider.getContact(contactId, SOURCE_DEVICE)
                    events.add(ContactEvent(eventType, eventDate, contact, eventId))
                } catch (e: DateParseException) {
                    tracker.track(e)
                } catch (e: ContactNotFoundException) {
                    Log.e(e)
                }

            }
        } finally {
            cursor!!.close()
        }
        return events
    }

    private fun getContactIdFrom(cursor: Cursor): Long {
        val contactIdIndex = cursor.getColumnIndex(ContactsContract.Data.CONTACT_ID)
        return cursor.getLong(contactIdIndex)
    }

    @Throws(DateParseException::class)
    private fun getEventDateFrom(cursor: Cursor): Date {
        val dateIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.START_DATE)
        val dateRaw = cursor.getString(dateIndex)

        return dateParser.parse(dateRaw)
    }

    private fun getEventTypeFrom(cursor: Cursor): EventType {
        val eventTypeIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.TYPE)
        val eventTypeRaw = cursor.getInt(eventTypeIndex)
        return when (eventTypeRaw) {
            ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY -> StandardEventType.BIRTHDAY
            ContactsContract.CommonDataKinds.Event.TYPE_ANNIVERSARY -> StandardEventType.ANNIVERSARY
            ContactsContract.CommonDataKinds.Event.TYPE_CUSTOM -> StandardEventType.CUSTOM
            else -> StandardEventType.OTHER
        }
    }

    private fun isInvalid(cursor: Cursor?): Boolean {
        return cursor == null || cursor.isClosed
    }

    companion object {

        private val CONTENT_URI = ContactsContract.Data.CONTENT_URI
        private val PROJECTION = arrayOf(
                ContactsContract.Data.CONTACT_ID,
                ContactsContract.CommonDataKinds.Event.TYPE,
                ContactsContract.CommonDataKinds.Event._ID,
                ContactsContract.CommonDataKinds.Event.START_DATE
        )
        private const val SELECTION = (
                "( " + ContactsContract.Data.MIMETYPE + " = ? "
                        + " AND " + ContactsContract.Data.IN_VISIBLE_GROUP + " = 1)")

        private val SELECT_ARGS = arrayOf(ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE)
        private val SORT_ORDER: String? = null

        private fun getEventIdFrom(cursor: Cursor): Long {
            val eventIdIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event._ID)
            return cursor.getLong(eventIdIndex)
        }
    }

}

