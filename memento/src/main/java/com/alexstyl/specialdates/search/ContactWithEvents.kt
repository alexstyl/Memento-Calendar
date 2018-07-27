package com.alexstyl.specialdates.search

import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.date.ContactEvent

data class ContactWithEvents(val contact: Contact, val events: List<ContactEvent>)
