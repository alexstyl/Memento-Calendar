package com.alexstyl.specialdates.person

import android.content.res.Resources
import android.support.v4.content.res.ResourcesCompat
import android.view.View
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.Strings
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.contact.ContactSource.SOURCE_FACEBOOK
import java.net.URI

class FacebookContactActionsProvider(
        private val strings: Strings,
        private val resources: Resources)
    : ContactActionsProvider {

    override fun callActionsFor(contact: Contact, actions: ContactActions): List<ContactActionViewModel> {
        ensureItsAFacebookContact(contact)
        val action = ContactAction(
                strings.call(),
                strings.facebookMessenger(),
                actions.view(URI.create("fb-messenger://user/" + contact.contactID)) // TODO check what happens if no messenger installed
        )
        return ContactActionViewModel(
                action,
                View.VISIBLE,
                ResourcesCompat.getDrawable(resources, R.drawable.ic_facebook_messenger, null)!!)
                .toList()
    }

    override fun messagingActionsFor(contact: Contact, actions: ContactActions): List<ContactActionViewModel> {
        ensureItsAFacebookContact(contact)
        return arrayListOf(
                goToWallAction(contact, actions),
                messengerAction(contact, actions))
    }

    private fun messengerAction(contact: Contact, executor: ContactActions): ContactActionViewModel = ContactActionViewModel(
            ContactAction(
                    strings.viewConversation(),
                    strings.facebookMessenger(),
                    executor.view(URI.create("fb-messenger://user/" + contact.contactID))
            ),
            View.VISIBLE,
            ResourcesCompat.getDrawable(resources, R.drawable.ic_facebook_messenger, null)!!)

    private fun goToWallAction(contact: Contact, executor: ContactActions): ContactActionViewModel = ContactActionViewModel(
            ContactAction(
                    strings.postOnFacebook(),
                    strings.facebook(),
                    executor.view(URI.create("https://www.facebook.com/profile.php?id=" + contact.contactID))
            ),
            View.VISIBLE,
            ResourcesCompat.getDrawable(resources, R.drawable.ic_f_icon, null)!!)

    private fun ensureItsAFacebookContact(contact: Contact) {
        if (contact.source != SOURCE_FACEBOOK) {
            throw IllegalArgumentException("Can only create actions for Facebook contacts. Asked for [$contact] instead")
        }
    }
}

private fun ContactActionViewModel.toList(): List<ContactActionViewModel> {
    val arrayList = ArrayList<ContactActionViewModel>()
    arrayList.add(this)
    return arrayList
}

