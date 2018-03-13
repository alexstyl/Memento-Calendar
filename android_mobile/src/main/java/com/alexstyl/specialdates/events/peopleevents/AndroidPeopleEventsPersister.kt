package com.alexstyl.specialdates.events.peopleevents

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.alexstyl.specialdates.CrashAndErrorTracker
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.contact.ContactSource
import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.events.database.DatabaseContract.AnnualEventsContract
import com.alexstyl.specialdates.events.database.EventColumns
import com.alexstyl.specialdates.events.database.EventTypeId.TYPE_NAMEDAY

class AndroidPeopleEventsPersister(private val helper: SQLiteOpenHelper,
                                   private val marshaller: ContactEventsMarshaller,
                                   private val tracker: CrashAndErrorTracker)
    : PeopleEventsPersister {

    override fun deleteAllNamedays() {
        helper.writableDatabase
                .executeTransaction { db ->
                    db.delete(
                            AnnualEventsContract.TABLE_NAME,
                            "${AnnualEventsContract.EVENT_TYPE}  ==  $TYPE_NAMEDAY",
                            null
                    )
                }
    }


    override fun deleteAllEventsOfSource(@ContactSource source: Int) {
        helper.writableDatabase
                .executeTransaction { db ->
                    db.delete(AnnualEventsContract.TABLE_NAME,
                            AnnualEventsContract.SOURCE + "==" + source,
                            null
                    )
                }
    }

    override fun deleteAllDeviceEvents() {
        helper.writableDatabase
                .executeTransaction { db ->
                    db.delete(AnnualEventsContract.TABLE_NAME,
                            "${EventColumns.SOURCE}  ==  ${ContactSource.SOURCE_DEVICE}" +
                                    " AND ${EventColumns.EVENT_TYPE}  != ${StandardEventType.NAMEDAY.id}"
                            , null)
                }
    }

    override fun insertAnnualEvents(events: List<ContactEvent>) {
        helper.writableDatabase
                .executeTransaction { db ->
                    marshaller
                            .marshall(events)
                            .forEach { contentValues ->
                                db.insert(AnnualEventsContract.TABLE_NAME, null, contentValues)
                            }
                }
    }

    override fun markContactAsVisible(contact: Contact) {
        helper.writableDatabase
                .executeTransaction { db ->
                    db.update(
                            AnnualEventsContract.TABLE_NAME,
                            visible(),
                            AnnualEventsContract.CONTACT_ID + " = " + contact.contactID
                                    + " AND " + AnnualEventsContract.SOURCE + " = " + contact.source, null
                    )

                }

    }

    private fun visible(): ContentValues {
        return ContentValues(1).apply {
            put(AnnualEventsContract.VISIBLE, 1)
        }
    }

    override fun markContactAsHidden(contact: Contact) {
        helper.writableDatabase
                .executeTransaction { db ->
                    val values = ContentValues(1)
                    values.put(AnnualEventsContract.VISIBLE, 0)

                    db.update(
                            AnnualEventsContract.TABLE_NAME,
                            values,
                            AnnualEventsContract.CONTACT_ID + " = " + contact.contactID
                                    + " AND " + AnnualEventsContract.SOURCE + " = " + contact.source, null
                    )
                }

    }

    override fun getVisibilityFor(contact: Contact): Boolean {
        val database = helper.writableDatabase
        // TODO just COUNT() events the contact has
        val query = database.query(
                AnnualEventsContract.TABLE_NAME, null,
                AnnualEventsContract.CONTACT_ID + " == " + contact.contactID
                        + " AND " + AnnualEventsContract.SOURCE + " == " + contact.source
                        + " AND " + AnnualEventsContract.VISIBLE + " = 1", null, null, null, null
        )
        val count = query.count
        query.close()
        return count > 0
    }

    private inline fun SQLiteDatabase.executeTransaction(function: (SQLiteDatabase) -> Unit) {
        try {
            this.beginTransaction()
            function(this)
            this.setTransactionSuccessful()
        } catch (e: SQLiteException) {
            tracker.track(e)
        } finally {
            this.endTransaction()
        }
    }
}

