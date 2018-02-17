package com.alexstyl.specialdates.contact;

import java.net.URI;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class ContactTest {

    private final URI someImageURI = URI.create("https://www.alexstyl.com/some/image.jpg");

    @Test
    public void toStringReturnsTheDisplayNameRepresentation() {
        Contact contact = new Contact(404, DisplayName.from("Alex Styl"), someImageURI, -1);
        assertThat(contact.toString()).isEqualTo("Alex Styl");
    }

}
