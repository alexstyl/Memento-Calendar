package com.alexstyl.specialdates.person

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.contact.ContactSource
import io.reactivex.Observable

class PersonCallProvider(val resources: Resources, val contentResolver: ContentResolver, val activity: Activity,
                         val androidContactActionsProvider: AndroidContactActionsProvider,
                         val facebookContactActionsProvider: FacebookContactActionsProvider) {

    fun getCallsFor(contact: Contact): Observable<List<ContactActionViewModel>> {
        return Observable.fromCallable {
            if (contact.source == ContactSource.SOURCE_FACEBOOK) {
                facebookContactActionsProvider.buildActionsFor(contact)
            } else {
                androidContactActionsProvider.buildActionsFor(contact)
            }
        }
    }

    private fun callViewModelFor(phoneNumber: String): ContactActionViewModel {
        val runnable = Runnable({
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber))
            activity.startActivity(intent)
        })
        // TODO tinted icons
        return ContactActionViewModel("Home", phoneNumber, resources.getDrawable(R.drawable.ic_facebook_messenger), runnable)
    }
}
