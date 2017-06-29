package com.alexstyl.specialdates.facebook.friendimport;

import android.net.Uri;

import com.alexstyl.specialdates.contact.DisplayName;
import com.alexstyl.specialdates.contact.Contact;

public class FacebookContact extends Contact {

    private final Uri imagePath;
    private final Uri lookupUri;

    public FacebookContact(long uid, DisplayName name, Uri imagePath) {
        super(uid, name);
        this.imagePath = imagePath;
        this.lookupUri = Uri.parse("https://www.facebook.com/" + uid);
    }

    @Override
    public Uri getLookupUri() {
        return lookupUri;
    }

    @Override
    public Uri getImagePath() {
        return imagePath;
    }
}
