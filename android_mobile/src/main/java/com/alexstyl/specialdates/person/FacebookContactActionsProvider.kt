package com.alexstyl.specialdates.person

import android.content.res.Resources
import android.support.v4.content.res.ResourcesCompat
import android.view.View
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.Strings
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
                ResourcesCompat.getDrawable(resources, R.drawable.ic_facebook_messenger, null)!!)
                .toList()
    }

    override fun messagingActionsFor(contact: Contact): List<ContactActionViewModel> = arrayListOf(goToWallAction(contact), messengerAction(contact))

    private fun messengerAction(contact: Contact): ContactActionViewModel = ContactActionViewModel(
            ContactAction(
                    strings.viewConversation(),
                    strings.facebookMessenger(),
                    actionsFactory.view(URI.create("fb-messenger://user/" + contact.contactID))
            ),
            View.VISIBLE,
            ResourcesCompat.getDrawable(resources, R.drawable.ic_facebook_messenger, null)!!)

    private fun goToWallAction(contact: Contact): ContactActionViewModel = ContactActionViewModel(
            ContactAction(
                    strings.postOnFacebook(),
                    strings.facebook(),
                    actionsFactory.view(URI.create("https://www.facebook.com/profile.php?id=" + contact.contactID))
            ),
            View.VISIBLE,
            ResourcesCompat.getDrawable(resources, R.drawable.ic_f_icon, null)!!)
}

private fun ContactActionViewModel.toList(): List<ContactActionViewModel> {
    val arrayList = ArrayList<ContactActionViewModel>()
    arrayList.add(this)
    return arrayList
}

