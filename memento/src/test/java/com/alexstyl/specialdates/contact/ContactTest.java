package com.alexstyl.specialdates.contact;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class ContactTest {

    private static final String SOME_IMAGE = "https://www.alexstyl.com/some/image.jpg";

    @Test
    public void toStringReturnsTheDisplayNameRepresentation() {
        Contact contact = new Contact(404, DisplayName.Companion.from("Alex Styl"), SOME_IMAGE, -1);
        assertThat(contact.toString()).isEqualTo("Alex Styl");
    }

}
