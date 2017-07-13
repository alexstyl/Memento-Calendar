package com.alexstyl.specialdates.contact;

import java.net.URI;

public final class Contact {

    private final long contactID;
    private final DisplayName displayName;
    private final URI imagePath;
    @ContactSource
    private final int source;

    public Contact(long id, DisplayName displayName, URI imagePath, int source) {
        this.contactID = id;
        this.displayName = displayName;
        this.imagePath = imagePath;
        this.source = source;
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

    @ContactSource
    public int getSource() {
        return source;
    }
}
