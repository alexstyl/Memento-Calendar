package com.alexstyl.specialdates;

import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.contact.DisplayName;

import java.net.URI;

public class TestContact extends Contact {
    private static final URI SOME_IMAGE = URI.create("https://www.google.com/image/to/something.jpg");

    public TestContact(long id, DisplayName displayName) {
        super(id, displayName, SOME_IMAGE);
    }

}
