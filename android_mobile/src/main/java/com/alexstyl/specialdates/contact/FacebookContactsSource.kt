package com.alexstyl.specialdates.contact

import android.database.Cursor
import com.alexstyl.specialdates.contact.ContactSource.SOURCE_FACEBOOK
import com.alexstyl.specialdates.events.database.DatabaseContract.AnnualEventsContract
import com.alexstyl.specialdates.events.database.EventSQLiteOpenHelper
import com.alexstyl.specialdates.facebook.FacebookImagePath

internal class FacebookContactsSource(private val eventSQLHelper: EventSQLiteOpenHelper,
                                      private val cache: ContactCache)
    : ContactsProviderSource {

    @Throws(ContactNotFoundException::class)
    override fun getOrCreateContact(contactID: Long): Contact {
        var contact: Contact? = cache.getContact(contactID)
        if (contact == null) {
            contact = queryContactWith(contactID)
        }
        return contact
    }

    @Throws(ContactNotFoundException::class)
    private fun queryContactWith(contactID: Long): Contact {
        val readableDatabase = eventSQLHelper.readableDatabase
        val cursor = readableDatabase.query(
                AnnualEventsContract.TABLE_NAME, null,
                "$IS_A_FACEBOOK_CONTACT AND ${AnnualEventsContract.CONTACT_ID} == $contactID", null, null, null, null
        )

        return cursor.use {
            if (!it.moveToFirst()) {
                throw ContactNotFoundException(contactID)
            }

            return@use createContactFrom(cursor)
        }
    }

    override fun queryContacts(contactIds: List<Long>): Contacts {
        val readableDatabase = eventSQLHelper.readableDatabase
        val cursor = readableDatabase.query(
                AnnualEventsContract.TABLE_NAME,
                null,
                "$IS_A_FACEBOOK_CONTACT AND ${AnnualEventsContract.CONTACT_ID} IN (${List(contactIds.size, { "?" }).joinToString(",")})",
                contactIds.map { it.toString() }.toTypedArray(),
                null,
                null,
                null
        )

        return cursor.use {
            val contacts = Contacts(SOURCE_FACEBOOK, List(it.count, { index ->
                it.moveToPosition(index)
                createContactFrom(it)
            }))
            cache.addContacts(contacts)
            return@use contacts
        }
    }

    override fun getAllContacts(): Contacts {
        return queryAllContacts().apply {
            cache.evictAll()
            cache.addContacts(this)
        }
    }

    private fun queryAllContacts(): Contacts {
        val db = eventSQLHelper.readableDatabase
        val cursor = db.rawQuery(
                "SELECT * FROM ${AnnualEventsContract.TABLE_NAME}" +
                        " WHERE ${AnnualEventsContract.SOURCE} == ? " +
                        " GROUP BY ${AnnualEventsContract.CONTACT_ID}",
                arrayOf(SOURCE_FACEBOOK.toString()))

        return cursor.use {
            return@use Contacts(SOURCE_FACEBOOK, List(it.count, { index ->
                it.moveToPosition(index)
                createContactFrom(it)
            }))
        }
    }

    companion object {

        private const val IS_A_FACEBOOK_CONTACT = AnnualEventsContract.SOURCE + "== " + SOURCE_FACEBOOK

        private fun createContactFrom(cursor: Cursor): Contact {
            val uid = cursor.getLong(cursor.getColumnIndexOrThrow(AnnualEventsContract.CONTACT_ID))
            val displayName = DisplayName.from(cursor.getString(cursor.getColumnIndexOrThrow(AnnualEventsContract.DISPLAY_NAME)))
            val imagePath = FacebookImagePath.forUid(uid)
            return Contact(uid, displayName, imagePath, SOURCE_FACEBOOK)
        }
    }
}
