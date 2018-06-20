package com.alexstyl.specialdates.contact

class ContactNotFoundException : Exception {
    constructor(contactId: Long) : super("Could not find contact with id [$contactId]")
    constructor(message: String) : super(message)
}
