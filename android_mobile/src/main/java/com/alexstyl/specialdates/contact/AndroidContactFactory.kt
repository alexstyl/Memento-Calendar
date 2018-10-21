package com.alexstyl.specialdates.contact

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.provider.ContactsContract
import com.alexstyl.specialdates.contact.AndroidContactsQuery.SORT_ORDER
import com.alexstyl.specialdates.contact.ContactSource.SOURCE_DEVICE
import java.net.URI

class AndroidContactFactory(private val resolver: ContentResolver) {

    fun getAllContacts(): List<Contact> {
        val cursor: Cursor = resolver.safeQuery(
                AndroidContactsQuery.CONTENT_URI,
                AndroidContactsQuery.PROJECTION,
                WHERE,
                null,
                SORT_ORDER
        )

        return cursor.use {
            return@use List(it.count) { index ->
                it.moveToPosition(index)
                createContactFrom(it)
            }
        }
    }

    @Throws(ContactNotFoundException::class)
    fun createContactWithId(contactID: Long): Contact {
        queryContactsWithContactId(contactID).use {
            if (it.moveToFirst()) {
                return createContactFrom(it)
            }
        }
        throw ContactNotFoundException(contactID)
    }


    fun queryContacts(ids: List<Long>): List<Contact> {
        val cursor = queryContactsWithContactId(ids)

        return cursor.use {
            return@use List(it.count) { index ->
                it.moveToPosition(index)
                createContactFrom(it)
            }
        }
    }

    private fun queryContactsWithContactId(ids: List<Long>): Cursor {
        return resolver.safeQuery(
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
        val imagePath = ContentUris.withAppendedId(AndroidContactsQuery.CONTENT_URI, contactID).toString()
        return Contact(contactID, displayName, imagePath, SOURCE_DEVICE)
    }

    private fun queryContactsWithContactId(contactID: Long): Cursor {
        return resolver.safeQuery(
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
        private const val WHERE = ContactsContract.Data.IN_VISIBLE_GROUP + " = 1"
        private const val SELECTION_CONTACT_WITH_ID = AndroidContactsQuery._ID + " = ?"
        private val emptyURI = URI.create("")

        private fun getContactIdFrom(cursor: Cursor): Long =
                cursor.getLong(AndroidContactsQuery.CONTACT_ID)


        private fun getDisplayNameFrom(cursor: Cursor): DisplayName {
            val string = cursor.getString(AndroidContactsQuery.DISPLAY_NAME)
            return DisplayName.from(string)
        }
    }

}

@SuppressLint("Recycle")
private fun ContentResolver.safeQuery(uri: Uri,
                                      projection: Array<String>?,
                                      selection: String?,
                                      selectionArgs: Array<String>?,
                                      sortOrder: String?): Cursor =
        this.query(uri, projection, selection, selectionArgs, sortOrder) ?: MatrixCursor(projection)

