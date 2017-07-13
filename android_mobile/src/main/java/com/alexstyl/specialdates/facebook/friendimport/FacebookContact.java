package com.alexstyl.specialdates.facebook.friendimport;

import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.contact.DisplayName;

import java.net.URI;

import static com.alexstyl.specialdates.contact.ContactSource.SOURCE_FACEBOOK;

public class FacebookContact extends Contact {
    public FacebookContact(long uid, DisplayName name, URI imagePath) {
        super(uid, name, imagePath, SOURCE_FACEBOOK);
    }
}
