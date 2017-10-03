package com.alexstyl.specialdates.search

import com.alexstyl.specialdates.contact.Contact
import java.net.URI

data class ContactEventViewModel(val contact: Contact,
                                 val displayName: String,
                                 val contactAvatarURI: URI,
                                 val eventLabel: String,
                                 val eventColor: Int,
                                 val backgroundVariant: Int)
