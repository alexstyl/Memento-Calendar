package com.alexstyl.specialdates.contact;

import java.net.URI;

import static com.alexstyl.specialdates.contact.ContactSource.SOURCE_DEVICE;

public class ContactFixture {

    private static final URI SOME_IMAGE = URI.create("https://www.alexstyl.com/image.jpg");

    public static Contact aContact() {
        return new Contact(-1, DisplayName.Companion.from("Test Contact"), SOME_IMAGE, SOURCE_DEVICE);
    }

    public static Contact aContactCalled(String peter) {
        return new Contact(-1, DisplayName.Companion.from(peter), SOME_IMAGE, 1);
    }

    public static Contact with(long id, String firstName) {
        return new Contact(id, DisplayName.Companion.from(firstName), SOME_IMAGE, SOURCE_DEVICE);
    }
}
