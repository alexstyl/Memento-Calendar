package com.alexstyl.specialdates.person

import com.alexstyl.resources.StringResources
import com.alexstyl.specialdates.R
import com.alexstyl.specialdates.contact.Contact
import java.net.URI

class FacebookContactActionsProvider(val stringResources: StringResources, val actionsFactory: ContactActionsFactory) {

    fun facebookCallFor(contact: Contact): ContactAction {
        return ContactAction(contact.contactID.toString(),
                stringResources.getString(R.string.facebook_messenger),
                actionsFactory.view(URI.create("fb-messenger://user/" + contact.contactID))
        )
    }

    fun facebookMessageFor(contact: Contact): ContactAction {
        return ContactAction(contact.contactID.toString(),
                stringResources.getString(R.string.facebook_messenger),
                actionsFactory.view(URI.create("fb-messenger://user/" + contact.contactID))
        )
    }

}

