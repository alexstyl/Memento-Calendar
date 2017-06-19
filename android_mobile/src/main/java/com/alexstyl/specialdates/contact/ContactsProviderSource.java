package com.alexstyl.specialdates.contact;

import com.alexstyl.specialdates.events.database.SourceType;

import java.util.List;

interface ContactsProviderSource {
    Contact getOrCreateContact(long contactID) throws ContactNotFoundException;

    List<Contact> getAllContacts();
}
