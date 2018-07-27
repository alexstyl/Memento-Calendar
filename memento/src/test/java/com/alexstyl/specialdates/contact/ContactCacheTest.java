package com.alexstyl.specialdates.contact;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class ContactCacheTest {

    private static final int CACHE_SIZE = 1024 * 5;

    private static final DisplayName ANY_DISPLAY_NAME = DisplayName.Companion.from("Alex Styl");

    private ContactCache contactCache;

    @Before
    public void createEmptyCache() {
        contactCache = new ContactCache(CACHE_SIZE);
    }

    @Test
    public void givenOneContactIsAdded_thenSizeIsIncreasedByOne() {
        Contact anyContact = ContactFixture.INSTANCE.aContact();

        contactCache.addContact(anyContact);

        assertThat(contactCache.size()).isEqualTo(1);
    }

    @Test
    public void givenOneContactIsAdded_thenSameContactIsReturned() {
        Contact anyContact = ContactFixture.INSTANCE.aContact();

        contactCache.addContact(anyContact);
        long contactID = anyContact.getContactID();

        Contact returnedContact = contactCache.getContact(contactID);

        assertThat(returnedContact).isEqualTo(anyContact);

    }

}
