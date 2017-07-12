package com.alexstyl.specialdates.addevent.ui;

import com.alexstyl.specialdates.contact.DisplayName;
import com.alexstyl.specialdates.TestContact;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.contact.ContactsProvider;
import com.alexstyl.specialdates.search.NameMatcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ContactsSearchTest {

    @Test
    public void ensureThatCounterIsRespected() {
        ContactsProvider contactsProvider = mock(ContactsProvider.class);
        when(contactsProvider.getAllContacts()).thenReturn(contacts("Alex Styl", "Alex Evil Twin"));
        ContactsSearch search = new ContactsSearch(contactsProvider, NameMatcher.INSTANCE);

        List<Contact> oneContact = search.searchForContacts("Alex", 1);
        assertThat(oneContact.size()).isEqualTo(1);

        List<Contact> twoContacts = search.searchForContacts("Alex", 2);
        assertThat(twoContacts.size()).isEqualTo(2);
    }

    @Test
    public void canFindFirstname() {
        ContactsProvider contactsProvider = mock(ContactsProvider.class);
        when(contactsProvider.getAllContacts()).thenReturn(contacts("Alex Styl", "Alex Evil Twin", "Anna Papadopoulou"));
        ContactsSearch search = new ContactsSearch(contactsProvider, NameMatcher.INSTANCE);

        List<Contact> oneContact = search.searchForContacts("Anna", 1);
        assertThat(oneContact.size()).isEqualTo(1);
        assertThat(oneContact.get(0).getDisplayName().toString()).isEqualTo("Anna Papadopoulou");
    }

    @Test
    public void canFindSurname() {
        ContactsProvider contactsProvider = mock(ContactsProvider.class);
        when(contactsProvider.getAllContacts()).thenReturn(contacts("Alex Styl", "Alex Evil Twin", "Anna Papadopoulou"));
        ContactsSearch search = new ContactsSearch(contactsProvider, NameMatcher.INSTANCE);

        List<Contact> oneContact = search.searchForContacts("Papadopoulou", 1);
        assertThat(oneContact.size()).isEqualTo(1);
        assertThat(oneContact.get(0).getDisplayName().toString()).isEqualTo("Anna Papadopoulou");
    }

    @Test
    public void returnEmptyForNoMatches() {
        ContactsProvider contactsProvider = mock(ContactsProvider.class);
        when(contactsProvider.getAllContacts()).thenReturn(contacts("Alex Styl", "Alex Evil Twin", "Anna Papadopoulou"));
        ContactsSearch search = new ContactsSearch(contactsProvider, NameMatcher.INSTANCE);
        List<Contact> results = search.searchForContacts("there is no contact with a name like this", 1);
        assertThat(results).isEmpty();
    }

    @Test
    public void returnEmptyForNoContacts() {
        ContactsProvider contactsProvider = mock(ContactsProvider.class);
        when(contactsProvider.getAllContacts()).thenReturn(Collections.<Contact>emptyList());
        ContactsSearch search = new ContactsSearch(contactsProvider, NameMatcher.INSTANCE);
        List<Contact> results = search.searchForContacts("there is no contact with a name like this", 1);
        assertThat(results).isEmpty();
    }

    private static List<Contact> contacts(String firstName, String... names) {
        long id = 0;
        ArrayList<Contact> contacts = new ArrayList<>();
        contacts.add(new TestContact(id, DisplayName.from(firstName)));
        id++;
        for (String name : names) {
            contacts.add(new TestContact(id, DisplayName.from(name)));
        }
        return contacts;
    }

}
