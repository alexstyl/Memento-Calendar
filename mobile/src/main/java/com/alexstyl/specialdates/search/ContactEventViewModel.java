package com.alexstyl.specialdates.search;

import android.net.Uri;

import com.alexstyl.specialdates.contact.Contact;

class ContactEventViewModel {
    private final Contact contact;
    private final String eventLabel;
    private final int backgroundVariant;

    ContactEventViewModel(Contact contact, String eventLabel, int backgroundVariant) {
        this.contact = contact;
        this.eventLabel = eventLabel;
        this.backgroundVariant = backgroundVariant;
    }

    int getBackgroundVariant() {
        return backgroundVariant;
    }

    String getDisplayName() {
        return contact.getDisplayName().toString();
    }

    Uri getContactAvatarURI() {
        return contact.getImagePath();
    }

    String getEventLabel() {
        return eventLabel;
    }

    Contact getContact() {
        return contact;
    }
}
