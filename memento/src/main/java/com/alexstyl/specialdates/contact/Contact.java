package com.alexstyl.specialdates.contact;

import java.net.URI;

public abstract class Contact {

    private final long contactID;
    private final DisplayName displayName;
    private final URI imagePath;

    protected Contact(long id, DisplayName displayName, URI imagePath) {
        this.contactID = id;
        this.displayName = displayName;
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return displayName.toString();
    }

    final public long getContactID() {
        return contactID;
    }

    final public DisplayName getDisplayName() {
        return displayName;
    }

    final public String getGivenName() {
        return displayName.getFirstNames().getPrimary();
    }

    final public URI getImagePath() {
        return imagePath;
    }

}
