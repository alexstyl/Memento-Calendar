package com.alexstyl.specialdates.contact;

import java.util.List;

interface ContactsProviderSource {
    Contact getOrCreateContact(long contactID) throws ContactNotFoundException;

    List<Contact> getAllContacts();

    List<Contact> getContacts(List<Long> contactIds);
}
