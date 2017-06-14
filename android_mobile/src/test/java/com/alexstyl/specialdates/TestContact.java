package com.alexstyl.specialdates;

import android.net.Uri;

import com.alexstyl.specialdates.contact.Contact;

public class TestContact extends Contact {

    public TestContact(long id, DisplayName displayName) {
        super(id, displayName);
    }

    @Override
    public Uri getLookupUri() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public Uri getImagePath() {
        throw new UnsupportedOperationException("Not supported");
    }

}
