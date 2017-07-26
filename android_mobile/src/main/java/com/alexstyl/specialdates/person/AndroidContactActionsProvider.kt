package com.alexstyl.specialdates.person

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.provider.ContactsContract.CommonDataKinds.Email
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.provider.ContactsContract.Data
import android.view.View
import com.alexstyl.resources.StringResources
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.theming.AttributeExtractor
import com.alexstyl.specialdates.theming.DrawableTinter
import com.novoda.notils.exception.DeveloperError
import java.net.URI


class AndroidContactActionsProvider(
        private val contentResolver: ContentResolver,
        private val strings: StringResources,
        private val context: Context,
        private val packageManager: PackageManager,
        private val actionsFactory: ContactActionsFactory)
    : ContactCallActionsProvider, ContactMessagingActionsProvider {


    private val WHATSAPP_VIDEO_CALL = "vnd.android.cursor.item/vnd.com.whatsapp.video.call"
    private val WHATSAPP_VOIP_CALL = "vnd.android.cursor.item/vnd.com.whatsapp.voip.call"
    private val WHATSAPP_MESSAGE = "vnd.android.cursor.item/vnd.com.whatsapp.profile"
    private val TELEGRAM_MESSAGE = "vnd.android.cursor.item/vnd.org.telegram.messenger.android.profile"

    private val tinter = DrawableTinter(AttributeExtractor())

    override fun callActionsFor(contact: Contact): List<ContactActionViewModel> {

        val viewModels = ArrayList<ContactActionViewModel>()

        val projection = null // TODO pick up only the things you need
        val cursor = contentResolver.query(Data.CONTENT_URI,
                projection,
                Data.CONTACT_ID + " = ? AND " + Data.IN_VISIBLE_GROUP + " = 1",
                arrayOf(
                        contact.contactID.toString()
                ),
                Data.MIMETYPE
        )
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val mimeType = cursor.getString(cursor.getColumnIndex(Data.MIMETYPE))
                if (Phone.CONTENT_ITEM_TYPE == mimeType) {
                    val phoneNumber = getPhoneNumberFrom(cursor)
                    val customLabel = getCallLabelFrom(cursor)
                    val action = ContactAction(phoneNumber, customLabel, actionsFactory.dial(phoneNumber))
                    val icon = tinter.tintWithAccentColor(R.drawable.ic_call, context)
                    val labelVisibility = if (customLabel.isEmpty()) View.GONE else View.VISIBLE
                    viewModels.add(ContactActionViewModel(action, labelVisibility, icon))
                } else if (WHATSAPP_VOIP_CALL == mimeType || WHATSAPP_VIDEO_CALL == mimeType) {

                    viewModels.add(createActionFor(cursor, mimeType))
//                    val uri = ContentUris.withAppendedId(Data.CONTENT_URI, cursor.getLong(cursor.getColumnIndex(Data._ID)))
//                    val intent = Intent(Intent.ACTION_VIEW)
//                    intent.setDataAndType(uri, mimeType)
//                    val resolveInfos = packageManager.queryIntentActivities(intent, 0)
//                    if (resolveInfos != null && resolveInfos.isNotEmpty()) {
//                        val label = cursor.getString(cursor.getColumnIndex(Data.DATA3))
//                        val action = ContactAction(label, "", actionsFactory.view(URI.create(uri.toString()), mimeType))
//                        val icon = resolveInfos[0].loadIcon(packageManager)
//                        viewModels.add(ContactActionViewModel(action, View.GONE, icon))
//                    }
                }
            }
            cursor.close()
        }
        return viewModels
    }

    override fun messagingActionsFor(contact: Contact): List<ContactActionViewModel> {
        val viewModels = ArrayList<ContactActionViewModel>()

        val projection = null // TODO pick up only the things you need
        val cursor = contentResolver.query(Data.CONTENT_URI,
                projection,
                Data.CONTACT_ID + " = ? AND " + Data.IN_VISIBLE_GROUP + " = 1",
                arrayOf(
                        contact.contactID.toString()
                ),
                Data.MIMETYPE
        )
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val mimeType = cursor.getString(cursor.getColumnIndex(Data.MIMETYPE))
                // TODO email
                if (Phone.CONTENT_ITEM_TYPE == mimeType) {
                    val phoneNumber = getPhoneNumberFrom(cursor)
                    val customLabel = getCallLabelFrom(cursor)
                    val action = ContactAction(phoneNumber, customLabel, actionsFactory.message(phoneNumber))
                    val icon = tinter.tintWithAccentColor(R.drawable.ic_message, context)
                    val labelVisibility = if (customLabel.isEmpty()) View.GONE else View.VISIBLE
                    viewModels.add(ContactActionViewModel(action, labelVisibility, icon))
                } else if (Email.CONTENT_ITEM_TYPE == mimeType) {
                    val phoneNumber = getPhoneNumberFrom(cursor)
                    val customLabel = getCallLabelFrom(cursor)
                    val action = ContactAction(phoneNumber, customLabel, actionsFactory.email(phoneNumber))
                    val icon = tinter.tintWithAccentColor(R.drawable.ic_email, context)
                    val labelVisibility = if (customLabel.isEmpty()) View.GONE else View.VISIBLE
                    viewModels.add(ContactActionViewModel(action, labelVisibility, icon))
                } else if (WHATSAPP_MESSAGE == mimeType || TELEGRAM_MESSAGE == mimeType) {
                    val element = createActionFor(cursor, mimeType)
                    viewModels.add(element)
                }
            }
            cursor.close()
        }
        return viewModels
    }

    private fun createActionFor(cursor: Cursor, mimeType: String): ContactActionViewModel {
        val uri = ContentUris.withAppendedId(Data.CONTENT_URI, cursor.getLong(cursor.getColumnIndex(Data._ID)))
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, mimeType)
        val resolveInfos = packageManager.queryIntentActivities(intent, 0)
        if (resolveInfos != null && resolveInfos.isNotEmpty()) {
            val label = cursor.getString(cursor.getColumnIndex(Data.DATA3))
            val action = ContactAction(label, resolveInfos[0].loadLabel(packageManager).toString(), actionsFactory.view(URI.create(uri.toString()), mimeType))
            val icon = resolveInfos[0].loadIcon(packageManager)
            return ContactActionViewModel(action, View.VISIBLE, icon)
        }
        throw DeveloperError("HANDLE THIS CASE DUDE")
    }


    private fun getPhoneNumberFrom(cursor: Cursor) = cursor.getString(cursor.getColumnIndex(Phone.DATA1))


    private fun getCallLabelFrom(cursor: Cursor): String {
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
}


