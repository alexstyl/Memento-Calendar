package com.alexstyl.specialdates.facebook.friendimport;

import com.alexstyl.specialdates.contact.DisplayName;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.DateParseException;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class FacebookContactFactoryTest {

    private FacebookContactFactory factory;

    @Before
    public void setUp() {
        factory = new FacebookContactFactory();
    }

    @Test
    public void name() throws DateParseException, InvalidFacebookContactException {
        HashMap<String, String> map = new HashMap<>();
        map.put("UID", "b123124@facebook.com");
        map.put("DTSTART", "20180612");
        map.put("SUMMARY", "Thanasis Thomopoulos's birthday");
        ContactEvent contactEvent = factory.createContactFrom(map);
        Contact contact = contactEvent.getContact();
        assertThat(contact.getDisplayName()).isEqualTo(DisplayName.Companion.from("Thanasis Thomopoulos"));
        assertThat(contact.getContactID()).isEqualTo(123124);
    }
}
