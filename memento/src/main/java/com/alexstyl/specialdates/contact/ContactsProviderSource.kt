package com.alexstyl.specialdates.contact

interface ContactsProviderSource {

    val allContacts: Contacts

    @Throws(ContactNotFoundException::class)
    fun getOrCreateContact(contactID: Long): Contact

    fun queryContacts(contactIds: List<Long>): Contacts
}
