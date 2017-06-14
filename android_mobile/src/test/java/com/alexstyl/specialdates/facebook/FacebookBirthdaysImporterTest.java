package com.alexstyl.specialdates.facebook;

import com.alexstyl.specialdates.date.ContactEvent;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class FacebookBirthdaysImporterTest {

    private FacebookBirthdaysImporter importer;

    @Before
    public void setUp() throws Exception {
        importer = new FacebookBirthdaysImporter(new MockCalendarLoader(), new FacebookContactFactory());

    }

    @Test
    public void testTestYo() {
        List<ContactEvent> contacts = importer.fetchFriends();
        for (ContactEvent contact : contacts) {
            System.out.println(contact.getContact());
        }
    }
}
