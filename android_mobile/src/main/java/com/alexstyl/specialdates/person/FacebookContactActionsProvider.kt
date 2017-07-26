package com.alexstyl.specialdates.person

import android.content.res.Resources
import com.alexstyl.resources.StringResources
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.contact.Contact
import java.net.URI

class FacebookContactActionsProvider(val stringResources: StringResources, private val resources: Resources, val actionsFactory: ContactActionsFactory)
    : ContactCallActionsProvider, ContactMessagingActionsProvider {

    override fun callActionsFor(contact: Contact): List<ContactActionViewModel> {
        val action = ContactAction(contact.contactID.toString(), stringResources.getString(R.string.facebook_messenger),
                actionsFactory.view(URI.create("fb-messenger://user/" + contact.contactID))
        )
        return ContactActionViewModel(
                action,
                resources.getDrawable(R.drawable.ic_facebook_messenger))
                .toList()
    }

    override fun messagingActionsFor(contact: Contact): List<ContactActionViewModel> {
        val action = ContactAction(contact.contactID.toString(),
                stringResources.getString(R.string.facebook_messenger),
                actionsFactory.view(URI.create("fb-messenger://user/" + contact.contactID)))
        return ContactActionViewModel(
                action,
                resources.getDrawable(R.drawable.ic_facebook_messenger))
                .toList()
    }


}

private fun ContactActionViewModel.toList(): List<ContactActionViewModel> {
    val arrayList = ArrayList<ContactActionViewModel>()
    arrayList.add(this)
    return arrayList
}

