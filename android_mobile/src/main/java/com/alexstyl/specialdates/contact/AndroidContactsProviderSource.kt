package com.alexstyl.specialdates.contact

class AndroidContactsProviderSource(private val cache: ContactCache,
                                    private val factory: AndroidContactFactory
) : ContactsProviderSource {

    // TODO evict if issue

    @Throws(ContactNotFoundException::class)
    override fun getOrCreateContact(contactID: Long): Contact {
        var deviceContact = cache.getContact(contactID)
        if (deviceContact == null) {
            deviceContact = factory.createContactWithId(contactID)
            cache.addContact(deviceContact)
        }
        return deviceContact
    }

    override val allContacts: List<Contact>
        get() {
            val allContacts = factory.getAllContacts()
            cache.evictAll()
            cache.addContacts(allContacts)
            return allContacts
        }

    override fun queryContacts(contactIds: List<Long>): List<Contact> {
        val contacts = factory.queryContacts(contactIds)
        cache.addContacts(contacts)
        return contacts
    }
}
