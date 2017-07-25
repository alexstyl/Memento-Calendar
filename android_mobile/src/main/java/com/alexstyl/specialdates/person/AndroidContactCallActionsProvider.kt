package com.alexstyl.specialdates.person

import android.content.ContentResolver
import android.content.ContentUris
import android.database.Cursor
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.provider.ContactsContract.Contacts
import android.provider.ContactsContract.Data
import com.alexstyl.resources.StringResources
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.contact.Contact
import java.net.URI


class AndroidContactCallActionsProvider(private val contentResolver: ContentResolver, private val strings: StringResources, private val actionsFactory: ContactActionsFactory) {

    private val WHATSAPP_VIDEO_CALL = "vnd.android.cursor.item/vnd.com.whatsapp.video.call"
    private val WHATSAPP_VOIP_CALL = "vnd.android.cursor.item/vnd.com.whatsapp.voip.call"
    private val WHATSAPP_MESSAGE = "vnd.android.cursor.item/vnd.com.whatsapp.profile"

    fun callActionsFor(contact: Contact): List<ContactAction> {
        val list = ArrayList<ContactAction>()

        val cursor = contentResolver.query(Data.CONTENT_URI,
                null,
                Contacts.Data.MIMETYPE + " = ? AND " + Data.CONTACT_ID + " = ? AND " + Data.IN_VISIBLE_GROUP + " = 1",
                arrayOf(
                        Phone.CONTENT_ITEM_TYPE,
                        contact.contactID.toString()
                ),
                null
        )
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val phoneNumber = cursor.getString(cursor.getColumnIndex(Phone.DATA1))
                val customLabel = getLabelFrom(cursor)
                list.add(ContactAction(phoneNumber, customLabel, actionsFactory.dialNumber(phoneNumber)))
            }
            cursor.close()
        }
        return list
    }

    private fun getLabelFrom(cursor: Cursor): String {
        val type = cursor.getInt(cursor.getColumnIndex(Phone.TYPE))
        if (type != Phone.TYPE_CUSTOM) {
            when (type) {
                Phone.TYPE_HOME -> return strings.getString(R.string.phone_type_home)
                Phone.TYPE_WORK -> return strings.getString(R.string.phone_type_work)
                Phone.TYPE_MOBILE -> return strings.getString(R.string.phone_type_mobile)
                else -> {
                    return strings.getString(R.string.Custom)
                }
            }
        } else {
            return cursor.getString(cursor.getColumnIndex(Phone.LABEL))
        }
    }

    fun customActionsFor(contact: Contact): List<ContactAction> {
        // stuff like Whatsapp, Skype, etc
        val list = ArrayList<ContactAction>()
        val cursor = contentResolver.query(Data.CONTENT_URI,
                null,
                Data.CONTACT_ID + " = ?",
                arrayOf(contact.contactID.toString()),
                null
        )

        if (cursor != null) {
            while (cursor.moveToNext()) {
                val mimeType = cursor.getString(cursor.getColumnIndex(Data.MIMETYPE))
                if (WHATSAPP_VOIP_CALL == mimeType || WHATSAPP_VIDEO_CALL == mimeType) {
                    val label = cursor.getString(cursor.getColumnIndex(Phone.LABEL))
                    val uri = ContentUris.withAppendedId(Data.CONTENT_URI, cursor.getLong(cursor.getColumnIndex(Data._ID)))
                    list.add(ContactAction("", label, actionsFactory.view(URI.create(uri.toString()), mimeType)))
                }
            }

            cursor.close()
        }
        return list
    }
}

