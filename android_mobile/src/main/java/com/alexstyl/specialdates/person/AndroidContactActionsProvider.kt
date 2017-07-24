package com.alexstyl.specialdates.person

import android.content.ContentResolver
import android.provider.ContactsContract
import com.alexstyl.specialdates.contact.Contact
import java.util.*
import kotlin.collections.ArrayList


class AndroidContactActionsProvider(val contentResolver: ContentResolver) : DeviceContactsActionsProvider {

    private val WHATSAPP_VIDEO_CALL = "vnd.android.cursor.item/vnd.com.whatsapp.video.call"
    private val WHATSAPP_VOIP_CALL = "vnd.android.cursor.item/vnd.com.whatsapp.voip.call"
    private val WHATSAPP_MESSAGE = "vnd.android.cursor.item/vnd.com.whatsapp.profile"

    override fun buildActionsFor(contact: Contact): List<ContactActionViewModel> {

        val list = ArrayList<ContactActionViewModel>()
        val cursor = contentResolver.query(ContactsContract.Data.CONTENT_URI, null, ContactsContract.Data.CONTACT_ID + " = ?", arrayOf(contact.contactID.toString()), null)

        // TODO add to list
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val mimeType = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.MIMETYPE))
                if (ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE == mimeType) {
                    // it's a phone!
                    val phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA1))
                    val type = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)) // Phone.TYPE_HOME etc
                    val customLabel = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL))
                    print("Phone number: $phoneNumber ($type/$customLabel)")
                } else if (somethingIDontCareAbout(mimeType)) {
                    //                print("Skipping: " + mimeType);
                } else if (isWhatsappMessage(mimeType)) {
                    print("Whatsapp Message ")
                } else if (isWhatsappVoiceCall(mimeType)) {
                    print("Whatsapp Voice Call ")
                } else if (isWhatsappVideoCall(mimeType)) {
                    print("Whatsapp Video Call ")
                } else {
                    print("___________________")
                    print("Custom type: " + mimeType)
                    for (columnName in cursor.getColumnNames()) {
                        try {
                            val value = cursor.getString(cursor.getColumnIndex(columnName))
                            print(columnName + " :: " + value)
                        } catch (e: Exception) {
                            print(columnName + " :: ERROR")
                        }

                    }
                    print("___________________")
                }
            }

            cursor.close()
        }
        return list
    }

    private fun isWhatsappVideoCall(mimeType: String): Boolean {
        return WHATSAPP_VIDEO_CALL == mimeType
    }


    private fun isWhatsappVoiceCall(mimeType: String): Boolean {
        return WHATSAPP_VOIP_CALL == mimeType
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
