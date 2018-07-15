package com.alexstyl.specialdates.events.peopleevents

import android.content.ContentResolver
import android.provider.ContactsContract

class CustomEventProvider(private val resolver: ContentResolver) {

    fun getEventWithId(deviceId: Long): EventType {

        resolver
                .query(CONTENT_URI,
                        PROJECTION,
                        SELECTION,
                        arrayOf(deviceId.toString(), ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE),
                        SORT_ORDER)
                .use { cursor ->
                    if (cursor.moveToFirst()) {
                        val columnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Event.LABEL)
                        val eventName = cursor.getString(columnIndex) ?: ""
                        return CustomEventType(eventName)
                    }
                }
        return StandardEventType.OTHER
    }

    companion object {

        private val CONTENT_URI = ContactsContract.Data.CONTENT_URI
        private val PROJECTION = arrayOf(ContactsContract.CommonDataKinds.Event.LABEL)
        private const val SELECTION = (
                "( " + ContactsContract.Data._ID + " = ?"
                        + " AND " + ContactsContract.Data.MIMETYPE + " = ? "
                        + " AND " + ContactsContract.Data.IN_VISIBLE_GROUP + " = 1"
                        + ")")

        private const val SORT_ORDER = ContactsContract.CommonDataKinds.Event._ID + " LIMIT 1"
    }
}
