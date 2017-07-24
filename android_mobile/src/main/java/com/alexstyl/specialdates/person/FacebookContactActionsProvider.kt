package com.alexstyl.specialdates.person

import android.app.Activity
import android.content.res.Resources
import com.alexstyl.resources.StringResources
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.contact.Contact
import java.net.URI

class FacebookContactActionsProvider(val activity: Activity, val stringResources: StringResources, val resources: Resources, val toRunnable: (uri: URI) -> Runnable) {
    fun buildActionsFor(contact: Contact): List<ContactActionViewModel> {
        return facebookCallFor(contact).toList()
    }

    private fun facebookCallFor(contact: Contact): ContactActionViewModel {
        val runnable = toRunnable(URI.create("fb-messenger://user/" + contact.contactID))
        return ContactActionViewModel(stringResources.getString(R.string.facebook_messenger),
                contact.displayName.toString(),
                resources.getDrawable(R.drawable.ic_facebook_messenger),
                runnable)
    }

}

private fun ContactActionViewModel.toList(): List<ContactActionViewModel> {
    val list = ArrayList<ContactActionViewModel>()
    list.add(this)
    return list
}
