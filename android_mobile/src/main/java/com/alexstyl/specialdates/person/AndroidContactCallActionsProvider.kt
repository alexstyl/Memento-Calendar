package com.alexstyl.specialdates.person

import android.content.ContentResolver
import android.database.Cursor
import android.provider.ContactsContract
import com.alexstyl.resources.StringResources
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.contact.Contact
import java.net.URI
import java.util.*
import kotlin.collections.ArrayList


class AndroidContactCallActionsProvider(private val contentResolver: ContentResolver, private val strings: StringResources, private val actionsFactory: ContactActionsFactory) {

    private val WHATSAPP_VIDEO_CALL = "vnd.android.cursor.item/vnd.com.whatsapp.video.call"
    private val WHATSAPP_VOIP_CALL = "vnd.android.cursor.item/vnd.com.whatsapp.voip.call"
    private val WHATSAPP_MESSAGE = "vnd.android.cursor.item/vnd.com.whatsapp.profile"

    fun callActionsFor(contact: Contact): List<ContactAction> {
        val list = ArrayList<ContactAction>()

        val cursor = contentResolver.query(ContactsContract.Data.CONTENT_URI,
                null,
                ContactsContract.Contacts.Data.MIMETYPE + " = ? AND " + ContactsContract.Data.CONTACT_ID + " = ?",
                arrayOf(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
                        contact.contactID.toString()
                ),
                null
        )
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA1))
                val customLabel = getPhoneLabelFor(cursor)
                list.add(ContactAction(phoneNumber, customLabel, actionsFactory.dialNumber(phoneNumber)))
            }
            cursor.close()
        }
        return list
    }

    private fun getPhoneLabelFor(cursor: Cursor): String {
        val type = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE))
        if (type != ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM) {
            when (type) {
                ContactsContract.CommonDataKinds.Phone.TYPE_HOME -> return strings.getString(R.string.phone_type_home)
                ContactsContract.CommonDataKinds.Phone.TYPE_WORK -> return strings.getString(R.string.phone_type_work)
                ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE -> return strings.getString(R.string.phone_type_mobile)
                else -> {
                    return strings.getString(R.string.Custom)
                }
            }
        } else {
            return cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL))
        }
    }

    fun customActionsFor(contact: Contact): List<ContactAction> {
        // stuff like Whatsapp, Skype, etc
        val list = ArrayList<ContactAction>()
        val cursor = contentResolver.query(ContactsContract.Data.CONTENT_URI,
                null,
                ContactsContract.Data.CONTACT_ID + " = ?",
                arrayOf(contact.contactID.toString()),
                null
        )

        if (cursor != null) {
            while (cursor.moveToNext()) {
                val mimeType = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.MIMETYPE))
                if (WHATSAPP_VOIP_CALL == mimeType || WHATSAPP_VIDEO_CALL == mimeType) {
                    val data = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA1))
                    val customLabel = getPhoneLabelFor(cursor)
                    list.add(ContactAction(data, customLabel, actionsFactory.view(URI.create(data))))
                }
            }

            cursor.close()
        }
        return list
    }


    private fun isWhatsappMessage(mimeType: String): Boolean {
        return WHATSAPP_MESSAGE == mimeType
    }

    private fun somethingIDontCareAbout(mimeType: String): Boolean {
        return Arrays.asList(
                ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Identity.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Relation.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE,
                ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE,
                "vnd.com.google.cursor.item/contact_misc"
        ).indexOf(mimeType) != -1
    }
}

