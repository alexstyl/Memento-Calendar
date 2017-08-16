package com.alexstyl.specialdates.person


import android.content.ActivityNotFoundException
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
import com.alexstyl.specialdates.ErrorTracker
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.theming.AttributeExtractor
import com.alexstyl.specialdates.theming.DrawableTinter
import java.net.URI


class AndroidContactActionsProvider(
        private val contentResolver: ContentResolver,
        private val strings: StringResources,
        private val context: Context,
        private val packageManager: PackageManager,
        private val actionsFactory: ContactActionsFactory)
    : ContactActionsProvider {


    private val WHATSAPP_VIDEO_CALL = "vnd.android.cursor.item/vnd.com.whatsapp.video.call"
    private val WHATSAPP_VOIP_CALL = "vnd.android.cursor.item/vnd.com.whatsapp.voip.call"
    private val VIBER_VOICE_MESSAGE = "vnd.android.cursor.item/vnd.com.viber.voip.google_voice_message"
    private val VIBER_NUMBER_CALL = "vnd.android.cursor.item/vnd.com.viber.voip.viber_number_call"
    private val VIBER_OUT_CALL = "vnd.android.cursor.item/vnd.com.viber.voip.viber_out_call_viber"

    private val WHATSAPP_MESSAGE = "vnd.android.cursor.item/vnd.com.whatsapp.profile"
    private val TELEGRAM_MESSAGE = "vnd.android.cursor.item/vnd.org.telegram.messenger.android.profile"
    private val VIBER_NUMBER_MESSAGE = "vnd.android.cursor.item/vnd.com.viber.voip.viber_number_message"


    private val CUSTOM_LABEL = Data.DATA3
    private val PROJECTION = arrayOf(
            Data._ID,
            Phone.NUMBER, // works for Email.ADDRESS
            Phone.TYPE, // works for Email.TYPE
            CUSTOM_LABEL, // works for custom events label
            Data.MIMETYPE
    )

    private val tinter = DrawableTinter(AttributeExtractor())

    override fun callActionsFor(contact: Contact): List<ContactActionViewModel> {

        val viewModels = ArrayList<ContactActionViewModel>()
        val cursor = contentResolver.query(Data.CONTENT_URI,
                PROJECTION,
                Data.CONTACT_ID + " = ? AND " + Data.IN_VISIBLE_GROUP + " = 1",
                arrayOf(
                        contact.contactID.toString()
                ),
                Data.MIMETYPE
        )
        cursor.use {
            c ->
            while (c.moveToNext()) {
                val mimeType = c.getString(c.getColumnIndex(Data.MIMETYPE))
                if (Phone.CONTENT_ITEM_TYPE == mimeType) {
                    val phoneNumber = getPhoneNumberFrom(c)
                    val customLabel = getCallLabelFrom(c)
                    val action = ContactAction(phoneNumber, customLabel, actionsFactory.dial(phoneNumber))
                    val icon = tinter.tintWithAccentColor(R.drawable.ic_call, context)
                    val labelVisibility = if (customLabel.isEmpty()) View.GONE else View.VISIBLE
                    val viewModel = ContactActionViewModel(action, labelVisibility, icon)
                    viewModels.add(viewModel)
                } else if (mimeType.isCustomCallType()) {
                    try {
                        val action = createActionFor(c, mimeType)
                        viewModels.add(action)
                    } catch (ex: ActivityNotFoundException) {
                        ErrorTracker.track(ex)
                    }
                }
            }
        }
        return viewModels
    }

    override fun messagingActionsFor(contact: Contact): List<ContactActionViewModel> {
        val viewModels = ArrayList<ContactActionViewModel>()

        val cursor = contentResolver.query(Data.CONTENT_URI,
                PROJECTION,
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
                    val action = ContactAction(phoneNumber, customLabel, actionsFactory.message(phoneNumber))
                    val icon = tinter.tintWithAccentColor(R.drawable.ic_message, context)
                    val labelVisibility = if (customLabel.isEmpty()) View.GONE else View.VISIBLE
                    viewModels.add(ContactActionViewModel(action, labelVisibility, icon))
                } else if (Email.CONTENT_ITEM_TYPE == mimeType) {
                    val phoneNumber = getEmailAddressFrom(cursor)
                    val customLabel = getEmailLabelFrom(cursor)
                    val action = ContactAction(phoneNumber, customLabel, actionsFactory.email(phoneNumber))
                    val icon = tinter.tintWithAccentColor(R.drawable.ic_email, context)
                    val labelVisibility = if (customLabel.isEmpty()) View.GONE else View.VISIBLE
                    viewModels.add(ContactActionViewModel(action, labelVisibility, icon))
                } else if (mimeType.isCustomMessagingType()) {
                    try {
                        val action = createActionFor(cursor, mimeType)
                        viewModels.add(action)
                    } catch (ex: ActivityNotFoundException) {
                        ErrorTracker.track(ex)
                    }
                }
            }
            cursor.close()
        }
        return viewModels
    }

    @Throws(ActivityNotFoundException::class)
    private fun createActionFor(cursor: Cursor, mimeType: String): ContactActionViewModel {
        val uri = ContentUris.withAppendedId(Data.CONTENT_URI, cursor.getLong(cursor.getColumnIndex(Data._ID)))
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, mimeType)
        val resolveInfos = packageManager.queryIntentActivities(intent, 0)
        if (resolveInfos != null && resolveInfos.isNotEmpty()) {
            val customLabel = cursor.getString(cursor.getColumnIndex(CUSTOM_LABEL))
            val viewAction = actionsFactory.view(URI.create(uri.toString()), mimeType)
            val label = resolveInfos[0].loadLabel(packageManager)
            val contactAction = ContactAction(customLabel, label.toString(), viewAction)
            val icon = resolveInfos[0].loadIcon(packageManager)
            return ContactActionViewModel(contactAction, View.VISIBLE, icon)
        }
        throw ActivityNotFoundException("Couldn't find activity for " + mimeType)
    }


    private fun getPhoneNumberFrom(cursor: Cursor) = cursor.getString(cursor.getColumnIndex(Phone.NUMBER))
    private fun getEmailAddressFrom(cursor: Cursor) = cursor.getString(cursor.getColumnIndex(Email.ADDRESS))


    private fun getCallLabelFrom(cursor: Cursor): String {
        val type = cursor.getInt(cursor.getColumnIndex(Phone.TYPE))
        return strings.getString(Phone.getTypeLabelResource(type))
    }

    private fun getEmailLabelFrom(cursor: Cursor): String {
        val type = cursor.getInt(cursor.getColumnIndex(Phone.TYPE))
        return strings.getString(Email.getTypeLabelResource(type))
    }

    private val CUSTOM_CALL_ACTION_LIST = arrayOf(
            WHATSAPP_VIDEO_CALL,
            WHATSAPP_VOIP_CALL,
            VIBER_VOICE_MESSAGE,
            VIBER_NUMBER_CALL,
            TELEGRAM_MESSAGE,
            VIBER_OUT_CALL
    )
    private val CUSTOM_MESSAGING_ACTION_LIST = arrayOf(
            WHATSAPP_MESSAGE,
            TELEGRAM_MESSAGE,
            VIBER_NUMBER_MESSAGE
    )

    private fun String.isCustomCallType(): Boolean {
        return CUSTOM_CALL_ACTION_LIST.contains(this)
    }

    private fun String.isCustomMessagingType(): Boolean {
        return CUSTOM_MESSAGING_ACTION_LIST.contains(this)
    }

}


