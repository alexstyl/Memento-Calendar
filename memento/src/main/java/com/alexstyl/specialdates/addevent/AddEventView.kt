package com.alexstyl.specialdates.addevent

import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.contact.ImageURL

interface AddEventView {

    fun display(uri: ImageURL)
    fun display(viewModels: List<AddEventContactEventViewModel>)
    fun displayContact(contact: Contact)

    fun allowImagePick()
    fun preventImagePick()

    fun allowSave()
    fun preventSave()
    
    fun clearAvatar()
}
