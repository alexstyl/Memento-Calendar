package com.alexstyl.specialdates.contact

import android.provider.ContactsContract
import android.provider.ContactsContract.Contacts

object AndroidContactsQuery {

    val CONTENT_URI = ContactsContract.Contacts.CONTENT_URI!!
    val PROJECTION = arrayOf(Contacts._ID, Contacts.DISPLAY_NAME_PRIMARY)
    const val SORT_ORDER = Contacts._ID

    const val CONTACT_ID = 0
    const val DISPLAY_NAME = 1
    const val _ID = Contacts._ID
}
