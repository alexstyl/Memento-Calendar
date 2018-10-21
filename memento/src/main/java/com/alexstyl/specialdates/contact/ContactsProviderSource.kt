package com.alexstyl.specialdates.contact

interface ContactsProviderSource {

    val allContacts: List<Contact>

    @Throws(ContactNotFoundException::class)
    fun getOrCreateContact(contactID: Long): Contact

    fun queryContacts(contactIds: List<Long>): List<Contact>
}
