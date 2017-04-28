package com.alexstyl.specialdates.contact;

import java.util.List;

public interface ContactsProvider {
    List<Contact> getAllContacts();

    Contact getOrCreateContact(long contactId) throws ContactNotFoundException;
}
