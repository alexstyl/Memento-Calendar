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

    public long getContactID() {
        return contactID;
    }

    public DisplayName getDisplayName() {
        return displayName;
    }

    public String getGivenName() {
        return displayName.getFirstNames().getPrimary();
    }

    public URI getImagePath() {
        return imagePath;
    }

}
