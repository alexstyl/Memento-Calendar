package com.alexstyl.specialdates.contact

import java.util.ArrayList
import java.util.Collections

class ContactsProvider(private val sources: Map<Int, ContactsProviderSource>) {

    val allContacts: List<Contact>
        get() {
            val contacts = ArrayList<Contact>()
            for (providerSource in sources.values) {
                contacts.addAll(providerSource.allContacts.contacts)
            }
            return Collections.unmodifiableList(contacts)
        }

    fun getContacts(contactIds: List<Long>, @ContactSource source: Int): Contacts {
        if (sources.containsKey(source)) {
            return sources[source]!!.queryContacts(contactIds)
        }
        throw IllegalArgumentException("Unknown source type: $source")
    }

    @Throws(ContactNotFoundException::class)
    fun getContact(contactID: Long, @ContactSource source: Int): Contact {
        if (sources.containsKey(source)) {
            return sources[source]!!.getOrCreateContact(contactID)
        }
        throw IllegalArgumentException("Unknown source type: $source")

    }

}
