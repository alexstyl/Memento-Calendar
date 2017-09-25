package com.alexstyl.specialdates.contact

class Contacts(val source: Int, val contacts: List<Contact>) : Iterable<Contact> {
    override fun iterator(): Iterator<Contact> = contacts.iterator()

    fun getContact(id: Long): Contact? = contacts.find { it.contactID == id }
}
