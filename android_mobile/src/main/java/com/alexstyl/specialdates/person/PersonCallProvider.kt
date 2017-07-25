package com.alexstyl.specialdates.person

import android.content.Context
import android.content.res.Resources
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.contact.ContactSource
import com.alexstyl.specialdates.theming.AttributeExtractor
import com.alexstyl.specialdates.theming.DrawableTinter
import io.reactivex.Observable

class PersonCallProvider(private val context: Context,
                         private val resources: Resources,
                         private val androidActionsProvider: AndroidContactCallActionsProvider,
                         private val facebookContactActionsProvider: FacebookContactActionsProvider) {

    private val tinter = DrawableTinter(AttributeExtractor())

    fun getCallsFor(contact: Contact): Observable<List<ContactActionViewModel>> {
        return Observable.fromCallable {
            if (contact.source == ContactSource.SOURCE_FACEBOOK) {
                val action = facebookContactActionsProvider.facebookCallFor(contact)
                arrayListOf(ContactActionViewModel(action, resources.getDrawable(R.drawable.ic_facebook_messenger)))
            } else if (contact.source == ContactSource.SOURCE_DEVICE) {
                val viewModels = ArrayList<ContactActionViewModel>()
                viewModels.addAll(deviceCallsOf(contact))
                viewModels.addAll(customCallsOf(contact))
                viewModels
            } else {
                throw IllegalArgumentException("unknown contact type " + contact.source)
            }
        }
    }

    private fun deviceCallsOf(contact: Contact): ArrayList<ContactActionViewModel> {
        val callActions = androidActionsProvider.callActionsFor(contact)
        val viewModels = ArrayList<ContactActionViewModel>()
        callActions.forEach {
            val icon = tinter.createAccentTintedDrawable(R.drawable.ic_call, context)
            viewModels.add(ContactActionViewModel(it, icon))
        }
        return viewModels
    }

    private fun customCallsOf(contact: Contact): Collection<ContactActionViewModel> {
        val callActions = androidActionsProvider.customActionsFor(contact)
        val viewModels = ArrayList<ContactActionViewModel>()
        callActions.forEach {
            val icon = tinter.createAccentTintedDrawable(R.drawable.ic_github, context)
            viewModels.add(ContactActionViewModel(it, icon))
        }
        return viewModels
    }

}
