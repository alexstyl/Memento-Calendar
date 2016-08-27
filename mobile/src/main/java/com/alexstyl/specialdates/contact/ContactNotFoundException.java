package com.alexstyl.specialdates.contact;

public class ContactNotFoundException extends Exception {

    public ContactNotFoundException(long contactId) {
        super("Could not find contact with id [" + contactId + "]");
    }
}
