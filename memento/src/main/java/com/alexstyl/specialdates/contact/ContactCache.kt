package com.alexstyl.specialdates.contact

import android.LruCache


class ContactCache(maxSize: Int) {

    private val cache: LruCache<Long, Contact> = LruCache(maxSize)

    fun addContact(contact: Contact) {
        cache.put(keyFor(contact), contact)
    }

    fun getContact(id: Long): Contact? = cache.get(id)

    fun size(): Int = cache.size()

    fun evictAll() {
        cache.evictAll()
    }

    fun addContacts(contacts: List<Contact>) {
        contacts.forEach {
            cache.put(it.contactID, it)
        }
    }

    private fun keyFor(contact: Contact): Long = contact.contactID
}
