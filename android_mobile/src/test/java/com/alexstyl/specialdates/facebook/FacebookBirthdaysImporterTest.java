package com.alexstyl.specialdates.facebook;

import com.alexstyl.specialdates.contact.Contact;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class FacebookBirthdaysImporterTest {

    private FacebookBirthdaysImporter importer;

    @Before
    public void setUp() throws Exception {
        importer = new FacebookBirthdaysImporter(new MockCalendarLoader());

    }

    @Test
    public void testTestYo() {
        List<Contact> contacts = importer.fetchFriends();
    }
}
