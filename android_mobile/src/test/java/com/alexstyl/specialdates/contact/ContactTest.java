package com.alexstyl.specialdates.contact;

import com.alexstyl.specialdates.TestContact;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class ContactTest {

    private Contact contact;
    private final DisplayName displayName = DisplayName.from("Alex Styl");

    @Before
    public void setUp() {
        contact = new TestContact(0, displayName);
    }

    @Test
    public void toStringReturnsTheDisplayNameRepresentation() {
        assertThat(contact.toString()).isEqualTo(displayName.toString());
    }

}
