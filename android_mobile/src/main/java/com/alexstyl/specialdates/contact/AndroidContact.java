package com.alexstyl.specialdates.contact;

import android.net.Uri;

import java.net.URI;

public class AndroidContact extends Contact {

    private final Uri lookupURI;

    AndroidContact(long contactId, DisplayName displayName, URI imagePath, Uri lookupURI) {
        super(contactId, displayName, imagePath);
        this.lookupURI = lookupURI;
    }

    public Uri getLookupUri() {
        return lookupURI;
    }

}
