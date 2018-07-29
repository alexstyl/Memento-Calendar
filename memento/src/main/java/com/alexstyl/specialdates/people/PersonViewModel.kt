package com.alexstyl.specialdates.people

import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.contact.ContactSource
import com.alexstyl.specialdates.contact.ImageURL


data class PersonViewModel(val contact: Contact,
                           val personName: String,
                           val avatarURI: ImageURL,
                           val personId: Long,
                           @ContactSource val personSource: Int) : PeopleRowViewModel

