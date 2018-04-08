package com.alexstyl.specialdates.addevent;

import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.contact.Contact;

final class SelectedContact {

    private final Optional<Contact> contact;
    private final String displayName;

    static SelectedContact anonymous() {
        return new SelectedContact(Optional.Companion.<Contact>absent(), "");
    }

    static SelectedContact anonymous(String displayName) {
        return new SelectedContact(Optional.Companion.<Contact>absent(), displayName);
    }

    private SelectedContact(Optional<Contact> contact, String displayName) {
        this.contact = contact;
        this.displayName = displayName;
    }

    boolean containsContact() {
        return contact.isPresent();
    }

    public Contact getContact() {
        if (contact.isPresent()) {
            return contact.get();
        }
        throw new IllegalStateException("Make sure that the SelectedContact contains a contact by calling #containsContact() first");
    }

    public String getDisplayName() {
        return displayName;
    }

    static SelectedContact forContact(Contact contact) {
        return new SelectedContact(new Optional<>(contact), contact.getDisplayName().toString());
    }
}
