package com.alexstyl.specialdates.people

import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.contact.ContactSource
import java.net.URI


data class PersonViewModel(val contact: Contact,
                           val personName: String,
                           val avatarURI: URI,
                           val personId: Long,
                           @ContactSource val personSource: Int) : PeopleRowViewModel

