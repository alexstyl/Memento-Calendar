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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ContactEventViewModel that = (ContactEventViewModel) o;

        if (backgroundVariant != that.backgroundVariant) {
            return false;
        }
        if (eventColor != that.eventColor) {
            return false;
        }
        if (!contact.equals(that.contact)) {
            return false;
        }
        return eventLabel.equals(that.eventLabel);

    }

    @Override
    public int hashCode() {
        int result = contact.hashCode();
        result = 31 * result + eventLabel.hashCode();
        result = 31 * result + backgroundVariant;
        result = 31 * result + eventColor;
        return result;
    }
}
