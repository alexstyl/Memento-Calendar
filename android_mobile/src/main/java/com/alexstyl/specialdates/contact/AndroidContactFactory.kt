package com.alexstyl.specialdates.contact

import android.content.ContentResolver
import android.content.ContentUris
import android.database.Cursor
import android.provider.ContactsContract
import android.provider.ContactsContract.Contacts
import com.alexstyl.specialdates.ErrorTracker
import com.alexstyl.specialdates.contact.AndroidContactsQuery.SORT_ORDER
import com.alexstyl.specialdates.contact.ContactSource.SOURCE_DEVICE
import java.net.URI
import java.util.*

internal class AndroidContactFactory(private val resolver: ContentResolver) {

    val allContacts: List<Contact>
        get() {
            val cursor = resolver.query(
                    AndroidContactsQuery.CONTENT_URI,
                    AndroidContactsQuery.PROJECTION,
                    WHERE, null,
                    AndroidContactsQuery.SORT_ORDER
            )
            val contacts = ArrayList<Contact>()
            try {
                while (cursor!!.moveToNext()) {
                    val contact = createContactFrom(cursor)
                    contacts.add(contact)
                }
            } catch (e: Exception) {
                ErrorTracker.track(e)
            } finally {
                cursor!!.close()
            }
            return Collections.unmodifiableList(contacts)
        }

    @Throws(ContactNotFoundException::class)
    fun createContactWithId(contactID: Long): Contact {
        val cursor = queryContactsWithContactId(contactID)
        if (isInvalid(cursor)) {
            throw RuntimeException("Cursor was invalid")
        }
        cursor.use { c ->
            if (c.moveToFirst()) {
                return createContactFrom(c)
            }
        }
        throw ContactNotFoundException(contactID)
    }


    fun getContacts(ids: List<Long>): List<Contact> {
        val cursor = queryContactsWithContactId(ids)

        return cursor.use { c ->
            return@use List(c.count) { index ->
                c.moveToPosition(index)
                createContactFrom(c)
            }
        }
    }

    private fun queryContactsWithContactId(ids: List<Long>): Cursor {
        return resolver.query(
                AndroidContactsQuery.CONTENT_URI,
                AndroidContactsQuery.PROJECTION,
                "${Contacts._ID} IN (${Collections.nCopies(ids.size, "?").joinToString(",")})",
                ids.map { it.toString() }.toTypedArray(),
                SORT_ORDER
        )
    }

    private fun createContactFrom(cursor: Cursor): Contact {
        val contactID = getContactIdFrom(cursor)
        val displayName = getDisplayNameFrom(cursor)
        val imagePath = URI.create(ContentUris.withAppendedId(Contacts.CONTENT_URI, contactID).toString())
        return Contact(contactID, displayName, imagePath, SOURCE_DEVICE)
    }

    private fun queryContactsWithContactId(contactID: Long): Cursor {
        return resolver.query(
                AndroidContactsQuery.CONTENT_URI,
                AndroidContactsQuery.PROJECTION,
                SELECTION_CONTACT_WITH_ID,
                makeSelectionArgumentsFor(contactID),
                SORT_ORDER + " LIMIT 1"
        )
    }

    private fun makeSelectionArgumentsFor(contactID: Long): Array<String> =
            arrayOf(contactID.toString())

    companion object {

        private val WHERE = ContactsContract.Data.IN_VISIBLE_GROUP + "=1"
        private val SELECTION_CONTACT_WITH_ID = Contacts._ID + " = ?"

        private fun isInvalid(cursor: Cursor?): Boolean = cursor == null || cursor.isClosed

        private fun getContactIdFrom(cursor: Cursor): Long =
                cursor.getLong(AndroidContactsQuery.CONTACT_ID)

        private fun getDisplayNameFrom(cursor: Cursor): DisplayName =
                DisplayName.from(cursor.getString(AndroidContactsQuery.DISPLAY_NAME))
    }

}
