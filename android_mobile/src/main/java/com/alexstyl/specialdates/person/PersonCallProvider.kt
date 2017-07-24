package com.alexstyl.specialdates.person

import android.content.res.Resources
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.contact.ContactSource
import io.reactivex.Observable

class PersonCallProvider(private val resources: Resources,
                         private val androidActionsProvider: AndroidContactCallActionsProvider,
                         private val facebookContactActionsProvider: FacebookContactActionsProvider) {

    fun getCallsFor(contact: Contact): Observable<List<ContactActionViewModel>> {
        return Observable.fromCallable {
            if (contact.source == ContactSource.SOURCE_FACEBOOK) {
                val action = facebookContactActionsProvider.facebookCallFor(contact)
                arrayListOf(ContactActionViewModel(action, resources.getDrawable(R.drawable.ic_facebook_messenger)))
            } else {
                val callActions = androidActionsProvider.callActionsFor(contact)
                val viewModels = ArrayList<ContactActionViewModel>()
                callActions.forEach {
                    viewModels.add(ContactActionViewModel(it, resources.getDrawable(R.drawable.ic_call))) // TODO tint icon
                }
                viewModels
            }
        }
    }

}
