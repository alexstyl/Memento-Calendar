package com.alexstyl.specialdates.contact

class ContactsProvider(private val sources: Map<Int, ContactsProviderSource>) {

    val allContacts: List<Contact>
        get() = sources.values.fold(emptyList(), { list, source ->
            list + source.allContacts
        })

    fun getContacts(contactIds: List<Long>, @ContactSource source: Int): Contacts {
        return sources[source]!!.queryContacts(contactIds)
    }

    @Throws(ContactNotFoundException::class)
    fun getContact(contactID: Long, @ContactSource source: Int): Contact {
        return sources[source]!!.getOrCreateContact(contactID)
    }

}
