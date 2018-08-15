package com.alexstyl.specialdates.events.peopleevents

import android.database.Cursor
import android.database.MergeCursor
import android.database.sqlite.SQLiteDatabase
import com.alexstyl.specialdates.CrashAndErrorTracker
import com.alexstyl.specialdates.Optional
import com.alexstyl.specialdates.SQLArgumentBuilder
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.contact.ContactNotFoundException
import com.alexstyl.specialdates.contact.ContactSource
import com.alexstyl.specialdates.contact.Contacts
import com.alexstyl.specialdates.contact.ContactsProvider
import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.DateParser
import com.alexstyl.specialdates.date.TimePeriod
import com.alexstyl.specialdates.date.beggingOfYear
import com.alexstyl.specialdates.date.dateOn
import com.alexstyl.specialdates.date.endOfYear
import com.alexstyl.specialdates.events.database.DatabaseContract.AnnualEventsContract
import com.alexstyl.specialdates.events.database.EventSQLiteOpenHelper
import com.alexstyl.specialdates.events.database.EventTypeId
import com.alexstyl.specialdates.events.database.EventTypeId.TYPE_CUSTOM
import com.novoda.notils.logger.simple.Log

class AndroidPeopleEventsProvider(private val eventSQLHelper: EventSQLiteOpenHelper,
                                  private val contactsProvider: ContactsProvider,
                                  private val customEventProvider: CustomEventProvider,
                                  private val dateParser: DateParser,
                                  private val tracker: CrashAndErrorTracker,
                                  private val shortDateLabelCreator: ShortDateLabelCreator) : PeopleEventsProvider {

    override fun fetchEventsOn(date: Date): ContactEventsOnADate {
        return ContactEventsOnADate.createFrom(date, fetchEventsBetween(TimePeriod.between(date, date)))
    }

    override fun fetchEventsBetween(timePeriod: TimePeriod): List<ContactEvent> {
        val cursor = queryEventsFor(timePeriod)
        val contactEvents = ArrayList<ContactEvent>(cursor.count)

        val deviceIds = ArrayList<Long>(cursor.count)
        val facebookIds = ArrayList<Long>(cursor.count)

        while (cursor.moveToNext()) {
            val contactId = getContactIdFrom(cursor)
            val source = getContactSourceFrom(cursor)

            when (source) {
                ContactSource.SOURCE_DEVICE -> deviceIds.add(contactId)
                ContactSource.SOURCE_FACEBOOK -> facebookIds.add(contactId)
                else -> throw UnsupportedOperationException("Source $source not managed")
            }
        }

        val deviceContacts = contactsProvider.getContacts(deviceIds, ContactSource.SOURCE_DEVICE)
        val facebookContacts = contactsProvider.getContacts(facebookIds, ContactSource.SOURCE_FACEBOOK)
        val contacts = HashMap<Int, Contacts>()
        contacts[ContactSource.SOURCE_DEVICE] = deviceContacts
        contacts[ContactSource.SOURCE_FACEBOOK] = facebookContacts

        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            try {
                val contactsOfSource = contacts[getContactSourceFrom(cursor)]
                val contact = contactsOfSource?.getContact(getContactIdFrom(cursor))
                if (contact != null) {
                    val contactEvent = getContactEventFrom(cursor, contact)
                    contactEvents.add(contactEvent)
                }
            } catch (e: ContactNotFoundException) {
                tracker.track(e)
            }

            cursor.moveToNext()
        }
        cursor.close()
        return contactEvents.toList()
    }

    override fun fetchEventsFor(contact: Contact): List<ContactEvent> {
        val contactEvents = ArrayList<ContactEvent>()
        val cursor = queryEventsOf(contact)
        while (cursor.moveToNext()) {
            try {
                val contactEvent = getContactEventFrom(cursor, contact)
                contactEvents.add(contactEvent)
            } catch (e: ContactNotFoundException) {
                Log.w(e)
            }

        }
        cursor.close()
        return contactEvents.toList()
    }

    private fun queryEventsFor(timeDuration: TimePeriod): Cursor {
        return if (isWithinTheSameYear(timeDuration)) {
            queryPeopleEvents(timeDuration, AnnualEventsContract.DATE + " ASC")
        } else {
            queryAllYearsIn(timeDuration)
        }
    }

    private fun queryEventsOf(contact: Contact): Cursor {
        val selectArgs = arrayOf(contact.contactID.toString(), contact.source.toString())

        // query database
        return eventSQLHelper.readableDatabase.query(
                AnnualEventsContract.TABLE_NAME,
                PROJECTION,
                AnnualEventsContract.CONTACT_ID + " = ? "
                        + "AND " + AnnualEventsContract.SOURCE + " = ?",
                selectArgs, null, null, null
        )
    }

    private fun queryPeopleEvents(timePeriod: TimePeriod, sortOrder: String): Cursor {
        val selectArgs = arrayOf(SQLArgumentBuilder.dateWithoutYear(timePeriod.startingDate),
                SQLArgumentBuilder.dateWithoutYear(timePeriod.endingDate)
        )

        return eventSQLHelper.readableDatabase.query(
                AnnualEventsContract.TABLE_NAME,
                PROJECTION,
                DATE_BETWEEN_IGNORING_YEAR,
                selectArgs, null, null,
                sortOrder
        )
    }

    private fun queryAllYearsIn(timeDuration: TimePeriod): Cursor {
        val firstHalf = firstHalfOf(timeDuration)
        val cursors = arrayOfNulls<Cursor>(2)
        cursors[0] = queryPeopleEvents(firstHalf, AnnualEventsContract.DATE + " ASC")
        val secondHalf = secondHalfOf(timeDuration)
        cursors[1] = queryPeopleEvents(secondHalf, AnnualEventsContract.DATE + " ASC")
        return MergeCursor(cursors)
    }

    private fun isWithinTheSameYear(timeDuration: TimePeriod): Boolean {
        return timeDuration.startingDate.year == timeDuration.endingDate.year
    }

    override fun findClosestEventDateOnOrAfter(date: Date): Date? =
            eventSQLHelper
                    .readableDatabase
                    .queryFirstEventOnOrAfter(date)
                    .use { cursor ->
                        return if (cursor.moveToFirst()) {
                            return cursor.getDate().withYear(date.year!!)
                        } else {
                            null
                        }
                    }

    private fun Date.withYear(year: Int): Date = dateOn(this.dayOfMonth, this.month, year)

    private fun SQLiteDatabase.queryFirstEventOnOrAfter(date: Date): Cursor =
            query(
                    AnnualEventsContract.TABLE_NAME,
                    AndroidPeopleEventsProvider.PEOPLE_PROJECTION,
                    "${AndroidPeopleEventsProvider.DATE_COLUMN_WITHOUT_YEAR} >= ?",
                    monthAndDayOf(date),
                    null, null,
                    "${AndroidPeopleEventsProvider.DATE_COLUMN_WITHOUT_YEAR} ASC LIMIT 1")

    private fun monthAndDayOf(date: Date): Array<String> {
        return arrayOf(shortDateLabelCreator.createLabelWithNoYearFor(date))
    }

    private fun getEventType(cursor: Cursor): EventType {
        val eventTypeIndex = cursor.getColumnIndexOrThrow(AnnualEventsContract.EVENT_TYPE)
        @EventTypeId val rawEventType = cursor.getInt(eventTypeIndex)
        if (rawEventType == TYPE_CUSTOM) {
            val deviceEventIdFrom = getDeviceEventIdFrom(cursor)
            return if (deviceEventIdFrom.isPresent) {
                queryCustomEvent(deviceEventIdFrom.get())
            } else StandardEventType.OTHER
        }
        return StandardEventType.fromId(rawEventType)
    }

    @Throws(ContactNotFoundException::class)
    private fun getContactEventFrom(cursor: Cursor, contact: Contact): ContactEvent {
        val date = cursor.getDate()
        val eventType = getEventType(cursor)

        val eventId = getDeviceEventIdFrom(cursor)
        return ContactEvent(eventId, eventType, date, contact)
    }

    @ContactSource
    private fun getContactSourceFrom(cursor: Cursor): Int {
        val sourceTypeIndex = cursor.getColumnIndexOrThrow(AnnualEventsContract.SOURCE)
        return cursor.getInt(sourceTypeIndex)
    }

    private fun queryCustomEvent(deviceId: Long): EventType {
        return customEventProvider.getEventWithId(deviceId)
    }

    private fun Cursor.getDate(): Date {
        val index = getColumnIndexOrThrow(AnnualEventsContract.DATE)
        val rawDate = getString(index)
        return dateParser.parse(rawDate)
    }

    companion object {

        private const val DATE_FROM = "substr(" + AnnualEventsContract.DATE + ",-5) >= ?"
        private const val DATE_TO = "substr(" + AnnualEventsContract.DATE + ",-5) <= ?"
        private const val DATE_BETWEEN_IGNORING_YEAR = DATE_FROM + " AND " + DATE_TO + " AND " + AnnualEventsContract.VISIBLE + " == 1"
        private val PEOPLE_PROJECTION = arrayOf(AnnualEventsContract.DATE)
        private val PROJECTION = arrayOf(
                AnnualEventsContract.CONTACT_ID,
                AnnualEventsContract.DEVICE_EVENT_ID,
                AnnualEventsContract.DATE,
                AnnualEventsContract.EVENT_TYPE,
                AnnualEventsContract.SOURCE
        )

        /*
        We use this column in order to be able to do comparisons of dates, without having to worry about the year
        So, instead of a full date 1990-12-19, this will return 12-19. Similarwise for --12-19.

        an example in use: select * from annual_events WHERE substr(date,-5) >= '03-04' ORDER BY substr(date,-5) asc LIMIT 1
     */
        private const val DATE_COLUMN_WITHOUT_YEAR = "substr(" + AnnualEventsContract.DATE + ", -5) "

        private fun firstHalfOf(timeDuration: TimePeriod): TimePeriod {
            return TimePeriod.between(
                    timeDuration.startingDate,
                    endOfYear(timeDuration.startingDate.year!!)
            )
        }

        private fun secondHalfOf(timeDuration: TimePeriod): TimePeriod {
            return TimePeriod.between(
                    beggingOfYear(timeDuration.endingDate.year!!),
                    timeDuration.endingDate
            )
        }


        private fun getContactIdFrom(cursor: Cursor): Long {
            val contactIdIndex = cursor.getColumnIndexOrThrow(AnnualEventsContract.CONTACT_ID)
            return cursor.getLong(contactIdIndex)
        }

        private fun getDeviceEventIdFrom(cursor: Cursor): Optional<Long> {
            val eventId = cursor.getColumnIndexOrThrow(AnnualEventsContract.DEVICE_EVENT_ID)
            val deviceEventId = cursor.getLong(eventId)
            return if (isALegitEventId(deviceEventId)) {
                Optional.absent()
            } else Optional(deviceEventId)
        }

        private fun isALegitEventId(deviceEventId: Long): Boolean {
            return deviceEventId == -1L
        }

    }

}



