package com.alexstyl.specialdates.addevent.ui;

import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.search.NameMatcher;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ContactsSearchTest {

    private ContactsSearch search;

    @Before
    public void setUp() {
        search = new ContactsSearch(new DummyContactsProvider(), NameMatcher.INSTANCE);
    }

    @Test
    public void ensureThatCounterIsRespected() {
        List<Contact> oneContact = search.searchForContacts("Alex", 1);
        assertThat(oneContact.size()).isEqualTo(1);

        List<Contact> twoContacts = search.searchForContacts("Alex", 2);
        assertThat(twoContacts.size()).isEqualTo(2);
    }

    @Test
    public void canFindFirstName() {
        List<Contact> oneContact = search.searchForContacts("Anna", 1);
        assertThat(oneContact.size()).isEqualTo(1);
        assertThat(oneContact.get(0).getDisplayName().toString()).isEqualTo("Anna Papadopoulou");
    }

    @Test
    public void canFindLastName() {
        List<Contact> oneContact = search.searchForContacts("Papadopoulou", 1);
        assertThat(oneContact.size()).isEqualTo(1);
        assertThat(oneContact.get(0).getDisplayName().toString()).isEqualTo("Anna Papadopoulou");
    }

}
