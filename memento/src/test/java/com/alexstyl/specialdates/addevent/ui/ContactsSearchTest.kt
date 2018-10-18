package com.alexstyl.specialdates.addevent.ui

import com.alexstyl.specialdates.addevent.ContactsSearch
import com.alexstyl.specialdates.contact.ContactsProvider
import com.alexstyl.specialdates.contact.ContactsProviderSource
import com.alexstyl.specialdates.search.NameMatcher
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ContactsSearchTest {

    private lateinit var contactsProvider: ContactsProvider
    @Mock
    private lateinit var mockSource: ContactsProviderSource

    @Before
    fun setUp() {
        contactsProvider = ContactsProvider(mapOf(Pair(1, mockSource)))

    }

    @Test
    fun ensureThatCounterIsRespected() {
        given(mockSource.allContacts).willReturnContacts("Alex Styl", "Alex Evil Twin")
        val search = ContactsSearch(contactsProvider, NameMatcher)
        val oneContact = search.searchForContacts("Alex", 1)
        assertThat(oneContact.size).isEqualTo(1)

        val twoContacts = search.searchForContacts("Alex", 2)
        assertThat(twoContacts.size).isEqualTo(2)
    }

    @Test
    fun canFindFirstname() {
        given(mockSource.allContacts).willReturnContacts("Alex Styl", "Alex Evil Twin", "Anna Papadopoulou")
        val search = ContactsSearch(contactsProvider, NameMatcher)

        val oneContact = search.searchForContacts("Anna", 1)
        assertThat(oneContact.size).isEqualTo(1)
        assertThat(oneContact[0].displayName.toString()).isEqualTo("Anna Papadopoulou")
    }

    @Test
    fun canFindSurname() {
        given(mockSource.allContacts).willReturnContacts("Alex Styl", "Alex Evil Twin", "Anna Papadopoulou")
        val search = ContactsSearch(contactsProvider, NameMatcher)

        val oneContact = search.searchForContacts("Papadopoulou", 1)
        assertThat(oneContact.size).isEqualTo(1)
        assertThat(oneContact[0].displayName.toString()).isEqualTo("Anna Papadopoulou")
    }

    @Test
    fun returnEmptyForNoMatches() {
        given(mockSource.allContacts).willReturnContacts("Alex Styl", "Alex Evil Twin", "Anna Papadopoulou")
        val search = ContactsSearch(contactsProvider, NameMatcher)
        val results = search.searchForContacts("there is no contact with a name like this", 1)
        assertThat(results).isEmpty()
    }

    @Test
    fun returnEmptyForNoContacts() {
        given(mockSource.allContacts).willReturnNoContact()
        val search = ContactsSearch(contactsProvider, NameMatcher)
        val results = search.searchForContacts("there is no contact with a name like this", 1)
        assertThat(results).isEmpty()
    }
}

