package com.alexstyl.specialdates.contact

internal class AndroidContactsProviderSource(private val cache: ContactCache, private val factory: AndroidContactFactory) : ContactsProviderSource {

    @Throws(ContactNotFoundException::class)
    override fun getOrCreateContact(contactID: Long): Contact {
        var deviceContact = cache.getContact(contactID)
        if (deviceContact == null) {
            deviceContact = factory.createContactWithId(contactID)
            cache.addContact(deviceContact)
        }
        return deviceContact
    }

    override fun getAllContacts(): Contacts {
        val allContacts = factory.getAllContacts()
        cache.evictAll()
        cache.addContacts(allContacts)
        return allContacts
    }

    override fun queryContacts(contactIds: List<Long>): Contacts {
        val contacts = factory.queryContacts(contactIds)
        cache.addContacts(contacts)
        return contacts
    }
}
