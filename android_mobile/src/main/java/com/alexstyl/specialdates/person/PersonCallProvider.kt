package com.alexstyl.specialdates.person

import android.app.Activity
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.contact.ContactSource
import io.reactivex.Observable

class PersonCallProvider(val resources: Resources, val contentResolver: ContentResolver, val activity: Activity) {

    fun getCallsFor(contact: Contact): Observable<List<ContactActionViewModel>> {
        return Observable.fromCallable {
            val list = ArrayList<ContactActionViewModel>()
            if (contact.source == ContactSource.SOURCE_FACEBOOK) {
                list.add(facebookCallFor(contact))
            } else {
                // TODO find number
                val element = callViewModelFor("555-321-232")
                list.add(element)
            }
            list
        }
        // Find phone numbers of contact
        // whatsapp
        // skype
        // facebook messenger
    }

    private fun callViewModelFor(phoneNumber: String): ContactActionViewModel {
        val runnable = Runnable({
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber))
            activity.startActivity(intent)
        })
        return ContactActionViewModel("Home", phoneNumber, resources.getDrawable(R.drawable.ic_call), runnable)
    }

//    private fun phoneNumbers() {
//        val phones = java.util.ArrayList<Phone>()
//        val phoneCursor: Cursor?
//        val selectionArgs = arrayOf<String>(contactID.toString())
//
//        val contentUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
//
//        phoneCursor = resolver.query(contentUri, DataTypeQuery.PROJECTION,
//                DataTypeQuery.SELECTION,
//                selectionArgs, DataTypeQuery.SORTORDER
//        )
//
//        if (phoneCursor == null) {
//            return phones
//        }
//
//        try {
//            val colData1 = phoneCursor!!.getColumnIndex(ContactsContract.Data.DATA1)
//            val colData2 = phoneCursor.getColumnIndex(ContactsContract.Data.DATA2)
//            val colData3 = phoneCursor.getColumnIndex(ContactsContract.Data.DATA3)
//
//            while (phoneCursor.moveToNext()) {
//                val number = phoneCursor.getString(colData1)
//                val type = phoneCursor.getInt(colData2)
//                val label = phoneCursor.getString(colData3)
//
//                val data = Phone(number, type, label)
//                if (!phones.contains(data)) {
//                    phones.add(data)
//                }
//            }
//
//        } catch (e: Exception) {
//            ErrorTracker.track(e)
//        } finally {
//            if (phoneCursor != null && !phoneCursor!!.isClosed) {
//                phoneCursor!!.close()
//            }
//        }
//
//        return phones
//    }

    private fun facebookCallFor(contact: Contact): ContactActionViewModel {
        val runnable = Runnable({
            val uri = ContentUris.withAppendedId(Uri.parse("fb-messenger://user/"), contact.contactID)
            val startIntent = Intent(Intent.ACTION_VIEW, uri)
            activity.startActivity(startIntent)
        })

        return ContactActionViewModel("Messenger", contact.displayName.toString(), resources.getDrawable(R.drawable.ic_facebook_messenger), runnable)
    }
}
