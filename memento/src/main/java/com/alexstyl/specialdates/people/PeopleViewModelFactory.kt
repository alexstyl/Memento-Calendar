package com.alexstyl.specialdates.people

import com.alexstyl.specialdates.Strings
import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.facebook.FacebookUserSettings

class PeopleViewModelFactory(private val strings: Strings, private val facebookPreferences: FacebookUserSettings) {

    fun facebookViewModel(): PeopleRowViewModel {
        return if (facebookPreferences.isLoggedIn) {
            FacebookImportViewModel(strings.viewFacebookProfile())
        } else {
            FacebookImportViewModel(strings.importFromFacebook())
        }
    }

    fun noContactsViewModel(): PeopleRowViewModel {
        return NoContactsViewModel()
    }


    fun personViewModel(contact: Contact): PeopleRowViewModel = PersonViewModel(
            contact,
            contact.displayName.toString(),
            contact.imagePath,
            contact.contactID,
            contact.source)
}
