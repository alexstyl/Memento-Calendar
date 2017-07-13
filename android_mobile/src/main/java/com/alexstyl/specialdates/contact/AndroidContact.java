package com.alexstyl.specialdates.contact;

import android.net.Uri;

import java.net.URI;

import static com.alexstyl.specialdates.contact.ContactSource.SOURCE_DEVICE;

public class AndroidContact extends Contact {

    private final Uri lookupURI;

    AndroidContact(long contactId, DisplayName displayName, URI imagePath, Uri lookupURI) {
        super(contactId, displayName, imagePath, SOURCE_DEVICE);
        this.lookupURI = lookupURI;
    }

    public Uri getLookupUri() {
        return lookupURI;
    }

}
