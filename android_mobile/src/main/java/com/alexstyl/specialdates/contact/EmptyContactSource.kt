package com.alexstyl.specialdates.contact

class EmptyContactSource(val source: Int) : ContactsProviderSource {

    override val allContacts: Contacts
        get() = Contacts(source, emptyList())


    @Throws(ContactNotFoundException::class)
    override fun getOrCreateContact(contactID: Long): Contact {
        throw ContactNotFoundException(contactID)
    }

    override fun queryContacts(contactIds: List<Long>): Contacts = Contacts(source, emptyList())
}
