package com.alexstyl.specialdates.contact;

import com.alexstyl.specialdates.events.database.SourceType;

import java.util.List;

interface ContactsProviderSource {
    Contact getOrCreateContact(long contactID, @SourceType int sourceDevice) throws ContactNotFoundException;

    List<Contact> getAllContacts();
}
