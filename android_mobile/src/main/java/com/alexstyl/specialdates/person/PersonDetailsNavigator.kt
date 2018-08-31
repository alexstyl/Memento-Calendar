package com.alexstyl.specialdates.person


import android.app.Activity
import com.alexstyl.specialdates.ExternalNavigator
import com.alexstyl.specialdates.Optional
import com.alexstyl.specialdates.contact.Contact

class PersonDetailsNavigator(private val navigator: ExternalNavigator) {

    fun toViewContact(displayingContact: Optional<Contact>) {
        if (displayingContact.isPresent) {
            navigator.toContactDetails(displayingContact.get())
        }
    }
}
