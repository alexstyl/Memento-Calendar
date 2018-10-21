package com.alexstyl.specialdates.search

import com.alexstyl.specialdates.contact.ContactFixture
import com.alexstyl.specialdates.contact.ContactSource
import com.alexstyl.specialdates.contact.ContactSource.SOURCE_DEVICE
import com.alexstyl.specialdates.contact.ContactsProvider
import com.alexstyl.specialdates.contact.ContactsProviderSource
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PeopleEventsSearchTest {

    private lateinit var search: PeopleEventsSearch
    @Mock
    private lateinit var mockContactSource: ContactsProviderSource

    @Before
    fun setUp() {
        search = PeopleEventsSearch(ContactsProvider(mapOf(Pair(SOURCE_DEVICE, mockContactSource))), NameMatcher)
        given(mockContactSource.allContacts).willReturn(listOf(ALEX_STYL, MARIA_PAPADOPOULOU, MIMOZA))
    }

    @Test
    fun searchingByFirstLetter() {
        val actual = search.searchForContacts("A")

        assertThat(actual).containsOnly(ALEX_STYL)
    }

    @Test
    fun searchingByLastLetter() {
        val actual = search.searchForContacts("S")

        assertThat(actual).containsOnly(ALEX_STYL)
    }

    @Test
    fun searchingByFullName() {
        val actual = search.searchForContacts("Alex Styl")

        assertThat(actual).containsOnly(ALEX_STYL)
    }

    @Test
    fun multipleResults() {
        val actual = search.searchForContacts("M")

        assertThat(actual).containsAll(listOf(MIMOZA, MARIA_PAPADOPOULOU))
    }

    @Test
    fun requestOneResultReturnsOnlyOneResult() {
        val actual = search.searchForContacts("M")
        assertThat(actual).containsOnly(MARIA_PAPADOPOULOU)
    }

    companion object {
        private val ALEX_STYL = ContactFixture.aContactCalled("Alex Styl")
        private val MARIA_PAPADOPOULOU = ContactFixture.aContactCalled("Maria Papadopoulou")
        private val MIMOZA = ContactFixture.aContactCalled("Mimoza Dereks")
    }
}
