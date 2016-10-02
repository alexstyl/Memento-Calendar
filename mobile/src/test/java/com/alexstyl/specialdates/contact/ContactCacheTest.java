package com.alexstyl.specialdates.contact;

import com.alexstyl.specialdates.DisplayName;
import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.date.Date;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class ContactCacheTest {

    private static final int CACHE_SIZE = 1024 * 5;

    private static final DisplayName ANY_DISPLAY_NAME = DisplayName.from("Alex Styl");

    private ContactCache<Contact> contactCache;

    @Before
    public void createEmptyCache() {
        contactCache = new ContactCache<>(CACHE_SIZE);
    }

    @Test
    public void givenOneContactIsAdded_thenSizeIsIncreasedByOne() {
        Contact anyContact = anyContact();

        contactCache.addContact(anyContact);

        assertThat(contactCache.size()).isEqualTo(1);
    }

    @Test
    public void givenOneContactIsAdded_thenSameContactIsReturned() {
        Contact anyContact = anyContact();

        contactCache.addContact(anyContact);
        long contactID = anyContact.getContactID();

        Contact returnedContact = contactCache.getContact(contactID);

        assertThat(returnedContact).isEqualTo(anyContact);

    }

    private static Contact anyContact() {
        return new DeviceContact(5, ANY_DISPLAY_NAME, "any_lookup_key", Optional.<Date>absent());
    }

}
