package com.alexstyl.specialdates.contact;

import android.net.Uri;

public abstract class Contact {

    private final long contactID;
    private final DisplayName displayName;

    public Contact(long id, DisplayName displayName) {
        this.contactID = id;
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName.toString();
    }

    public long getContactID() {
        return contactID;
    }

    public DisplayName getDisplayName() {
        return displayName;
    }

    public String getGivenName() {
        return displayName.getFirstNames().getPrimary();
    }

    public abstract Uri getLookupUri();

    public abstract Uri getImagePath();

}
