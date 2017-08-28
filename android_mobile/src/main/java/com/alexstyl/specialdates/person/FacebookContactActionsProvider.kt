package com.alexstyl.specialdates.person

import android.content.res.Resources
import android.view.View
import com.alexstyl.resources.Strings
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.contact.Contact
import java.net.URI

class FacebookContactActionsProvider(
        private val strings: Strings,
        private val resources: Resources,
        private val actionsFactory: ContactActionsFactory)
    : ContactActionsProvider {

    override fun callActionsFor(contact: Contact): List<ContactActionViewModel> {
        val action = ContactAction(
                strings.viewConversation(),
                strings.facebookMessenger(),
                actionsFactory.view(URI.create("fb-messenger://user/" + contact.contactID)) // TODO check what happens if no messenger installed
        )
        return ContactActionViewModel(
                action,
                View.VISIBLE,
                resources.getDrawable(R.drawable.ic_facebook_messenger))
                .toList()
    }

    override fun messagingActionsFor(contact: Contact): List<ContactActionViewModel> {
        val action = ContactAction(
                strings.viewConversation(),
                strings.facebookMessenger(),
                actionsFactory.view(URI.create("fb-messenger://user/" + contact.contactID))
        )
        return ContactActionViewModel(
                action,
                View.VISIBLE,
                resources.getDrawable(R.drawable.ic_facebook_messenger))
                .toList()
    }


}

private fun ContactActionViewModel.toList(): List<ContactActionViewModel> {
    val arrayList = ArrayList<ContactActionViewModel>()
    arrayList.add(this)
    return arrayList
}

