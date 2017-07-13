package com.alexstyl.specialdates.contact;

import java.net.URI;

import org.junit.Test;
import org.mockito.Mock;

import static org.fest.assertions.api.Assertions.assertThat;

public class ContactTest {

    @Mock
    private URI mockImagePath;

    @Test
    public void toStringReturnsTheDisplayNameRepresentation() {
        Contact contact = new Contact(404, DisplayName.from("Alex Styl"), mockImagePath, -1);
        assertThat(contact.toString()).isEqualTo("Alex Styl");
    }

}
