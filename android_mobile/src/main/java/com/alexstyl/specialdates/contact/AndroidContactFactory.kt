package com.alexstyl.specialdates.contact

import android.content.ContentResolver
import android.content.ContentUris
import android.database.Cursor
import android.provider.ContactsContract
import com.alexstyl.specialdates.contact.AndroidContactsQuery.SORT_ORDER
import com.alexstyl.specialdates.contact.ContactSource.SOURCE_DEVICE
import java.net.URI

class AndroidContactFactory(private val resolver: ContentResolver) {

    fun getAllContacts(): Contacts {
        val cursor: Cursor = resolver.query(
                AndroidContactsQuery.CONTENT_URI,
                AndroidContactsQuery.PROJECTION,
                WHERE,
                null,
                SORT_ORDER
        ) ?: return Contacts(SOURCE_DEVICE, emptyList())

        return cursor.use {
            return@use Contacts(SOURCE_DEVICE, List(it.count, { index ->
                it.moveToPosition(index)
                createContactFrom(it)
            }))
        }
    }

    @Throws(ContactNotFoundException::class)
    fun createContactWithId(contactID: Long): Contact {
        val cursor = queryContactsWithContactId(contactID)
        if (isInvalid(cursor)) {
            throw RuntimeException("Cursor was invalid")
        }
        cursor.use {
            if (it.moveToFirst()) {
                return createContactFrom(it)
            }
        }
        throw ContactNotFoundException(contactID)
    }


    fun queryContacts(ids: List<Long>): Contacts {
        val cursor = queryContactsWithContactId(ids)

        return cursor.use {
            return@use Contacts(SOURCE_DEVICE, List(it.count) { index ->
                it.moveToPosition(index)
                createContactFrom(it)
            })
        }
    }

    private fun queryContactsWithContactId(ids: List<Long>): Cursor {
        return resolver.query(
                AndroidContactsQuery.CONTENT_URI,
                AndroidContactsQuery.PROJECTION,
                "${AndroidContactsQuery._ID} IN (${ids.joinToString(",")})",
                null,
                SORT_ORDER
        )
    }

    private fun createContactFrom(cursor: Cursor): Contact {
        val contactID = getContactIdFrom(cursor)
        val displayName = getDisplayNameFrom(cursor)
        val imagePath = URI.create(ContentUris.withAppendedId(AndroidContactsQuery.CONTENT_URI, contactID).toString())
        return Contact(contactID, displayName, imagePath, SOURCE_DEVICE)
    }

    private fun queryContactsWithContactId(contactID: Long): Cursor {
        return resolver.query(
                AndroidContactsQuery.CONTENT_URI,
                AndroidContactsQuery.PROJECTION,
                SELECTION_CONTACT_WITH_ID,
                makeSelectionArgumentsFor(contactID),
                "$SORT_ORDER LIMIT 1"
        )
    }

    private fun makeSelectionArgumentsFor(contactID: Long): Array<String> =
            arrayOf(contactID.toString())

    companion object {
        private const val WHERE = ContactsContract.Data.IN_VISIBLE_GROUP + "=1"
        private const val SELECTION_CONTACT_WITH_ID = AndroidContactsQuery._ID + " = ?"

        private fun isInvalid(cursor: Cursor?): Boolean = cursor == null || cursor.isClosed

        private fun getContactIdFrom(cursor: Cursor): Long =
                cursor.getLong(AndroidContactsQuery.CONTACT_ID)

        private fun getDisplayNameFrom(cursor: Cursor): DisplayName =
                DisplayName.from(cursor.getString(AndroidContactsQuery.DISPLAY_NAME))
    }

}

