package com.alexstyl.specialdates.contact;

import java.util.List;

public interface ContactsProvider {
    List<Contact> fetchAllDeviceContacts();
}
