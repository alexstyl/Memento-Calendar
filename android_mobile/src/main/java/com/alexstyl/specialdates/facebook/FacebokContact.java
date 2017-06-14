package com.alexstyl.specialdates.facebook;

import android.net.Uri;

import com.alexstyl.specialdates.DisplayName;
import com.alexstyl.specialdates.contact.Contact;

class FacebokContact extends Contact {
    FacebokContact(long uid, DisplayName name) {
        super(uid, name);
    }

    @Override
    public Uri getLookupUri() {
        return null;
    }

    @Override
    public Uri getImagePath() {
        return null;
    }
}
