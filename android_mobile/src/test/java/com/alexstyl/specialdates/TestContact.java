package com.alexstyl.specialdates;

import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.contact.DisplayName;

import java.net.URI;

public class TestContact extends Contact {

    public TestContact(long id, DisplayName displayName) {
        super(id, displayName, URI.create("https://www.google.com/image/to/something.jpg"));
    }

    @Override
    public URI getImagePath() {
        throw new UnsupportedOperationException("Not supported");
    }

}
