package com.alexstyl.specialdates.contact;

import java.util.List;

interface ContactsProviderSource {
    Contact getOrCreateContact(long contactID) throws ContactNotFoundException;

    Contacts queryContacts(List<Long> contactIds);

    Contacts getAllContacts();
}
