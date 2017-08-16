package com.alexstyl.specialdates.contact

import android.database.Cursor
import com.alexstyl.specialdates.contact.ContactSource.SOURCE_FACEBOOK
import com.alexstyl.specialdates.events.database.DatabaseContract.AnnualEventsContract
import com.alexstyl.specialdates.events.database.EventSQLiteOpenHelper
import com.alexstyl.specialdates.facebook.FacebookImagePath

internal class FacebookContactsSource(private val eventSQLHelper: EventSQLiteOpenHelper, private val cache: ContactCache<Contact>) : ContactsProviderSource {

    @Throws(ContactNotFoundException::class)
    override fun getOrCreateContact(contactID: Long): Contact {
        var contact: Contact? = cache.getContact(contactID)
        if (contact ==
                null) {
            contact = queryContactWith(contactID)
        }
        return contact
    }

    @Throws(ContactNotFoundException::class)
    private fun queryContactWith(contactID: Long): Contact {
        val readableDatabase = eventSQLHelper.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = readableDatabase.query(
                    AnnualEventsContract.TABLE_NAME, null,
                    "$IS_A_FACEBOOK_CONTACT AND ${AnnualEventsContract.CONTACT_ID} == $contactID", null, null, null, null
            )
            if (cursor!!.moveToFirst()) {
                return createContactFrom(cursor)
            }
        } finally {
            if (cursor != null) {
                cursor.close()
            }
        }
        throw ContactNotFoundException(contactID)
    }

    override fun getContacts(contactIds: List<Long>): List<Contact>? {
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

        return cursor.use { c ->
            return@use List(c.count, { index ->
                c.moveToPosition(index)
                createContactFrom(c)
            })
        }
    }

    override fun getAllContacts(): List<Contact> {
        val allContacts = queryAllContacts()
        cache.evictAll()
        for (allContact in allContacts) {
            cache.addContact(allContact)
        }
        return allContacts
    }

    private fun queryAllContacts(): List<Contact> {
        val readableDatabase = eventSQLHelper.readableDatabase
        var cursor: Cursor? = null
        val contacts = ArrayList<Contact>()
        try {
            cursor = readableDatabase.query(
                    AnnualEventsContract.TABLE_NAME, null,
                    IS_A_FACEBOOK_CONTACT, null, null, null, null
            )
            if (cursor!!.moveToFirst()) {
                val contact = createContactFrom(cursor)
                contacts.add(contact)
            }
        } finally {
            if (cursor != null) {
                cursor.close()
            }
        }
        return contacts
    }

    companion object {

        private val IS_A_FACEBOOK_CONTACT = AnnualEventsContract.SOURCE + "== " + SOURCE_FACEBOOK

        private fun createContactFrom(cursor: Cursor): Contact {
            val uid = cursor.getLong(cursor.getColumnIndexOrThrow(AnnualEventsContract.CONTACT_ID))
            val displayName = DisplayName.from(cursor.getString(cursor.getColumnIndexOrThrow(AnnualEventsContract.DISPLAY_NAME)))
            val imagePath = FacebookImagePath.forUid(uid)
            return Contact(uid, displayName, imagePath, SOURCE_FACEBOOK)
        }
    }
}
