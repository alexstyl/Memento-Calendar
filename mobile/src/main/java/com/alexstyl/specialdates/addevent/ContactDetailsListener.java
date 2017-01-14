package com.alexstyl.specialdates.addevent;

import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.events.peopleevents.EventType;

interface ContactDetailsListener {
    void onAddEventClicked(ContactEventViewModel viewModel);
    void onRemoveEventClicked(EventType eventType);
    void onContactSelected(Contact contact);
    void onNameModified(String newName);
}
