package com.alexstyl.specialdates.search;

import android.support.annotation.ColorRes;

import com.alexstyl.specialdates.contact.Contact;

import java.net.URI;

class ContactEventViewModel {

    private final Contact contact;
    private final String eventLabel;
    private final int backgroundVariant;
    private final int eventColor;

    ContactEventViewModel(Contact contact, String eventLabel, int eventColor, int backgroundVariant) {
        this.contact = contact;
        this.eventLabel = eventLabel;
        this.eventColor = eventColor;
        this.backgroundVariant = backgroundVariant;
    }

    int getBackgroundVariant() {
        return backgroundVariant;
    }

    String getDisplayName() {
        return contact.getDisplayName().toString();
    }

    URI getContactAvatarURI() {
        return contact.getImagePath();
    }

    String getEventLabel() {
        return eventLabel;
    }

    Contact getContact() {
        return contact;
    }

    @ColorRes
    int getEventColor() {
        return eventColor;
    }
}
