package com.alexstyl.specialdates.search

import com.alexstyl.specialdates.contact.Contact
import com.alexstyl.specialdates.contact.ContactFixture
import com.alexstyl.specialdates.contact.ContactSource.SOURCE_DEVICE
import com.alexstyl.specialdates.contact.ContactsProvider
import com.alexstyl.specialdates.contact.ContactsProviderSource
import com.alexstyl.specialdates.contact.DisplayName
import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.date.Months.JANUARY
import com.alexstyl.specialdates.date.dateOn
import com.alexstyl.specialdates.events.peopleevents.EventType
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsProvider
import com.alexstyl.specialdates.events.peopleevents.StandardEventType
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PeopleSearchTest {

    private lateinit var search: PeopleSearch
    @Mock
    private lateinit var mockContactSource: PeopleEventsProvider

    @Before
    fun setUp() {
        search = PeopleSearch(mockContactSource, NameMatcher)
        given(mockContactSource.fetchAllEventsInAYear()).willReturn(
                listOf(
                        ContactEvent(StandardEventType.BIRTHDAY, aDate, ALEX_STYL, null),
                        ContactEvent(StandardEventType.BIRTHDAY, aDate, MARIA_PAPADOPOULOU, null),
                        ContactEvent(StandardEventType.BIRTHDAY, aDate, MIMOZA, null),
                        ContactEvent(StandardEventType.BIRTHDAY, aDate, Contact(5, DisplayName.from("Αλέξανδρος Χρονόπουλος"), "", 5), null)

                ))
    }

    @Test
    fun name() {
        val actual = search.searchForContacts("A").blockingFirst()
        assertThat(actual).contains(Contact(5, DisplayName.from("Αλέξανδρος Χρονόπουλος"), "", 5))

    }

    @Test
    fun searchingByFirstLetter() {
        val actual = search.searchForContacts("A").blockingFirst()

        assertThat(actual).containsOnly(ALEX_STYL)
    }

    @Test
    fun searchingByLastLetter() {
        val actual = search.searchForContacts("S").blockingFirst()

        assertThat(actual).containsOnly(ALEX_STYL)
    }

    @Test
    fun searchingByFullName() {
        val actual = search.searchForContacts("Alex Styl").blockingFirst()

        assertThat(actual).containsOnly(ALEX_STYL)
    }

    @Test
    fun multipleResults() {
        val actual = search.searchForContacts("M").blockingFirst()

        assertThat(actual).containsAll(listOf(MIMOZA, MARIA_PAPADOPOULOU))
    }

    companion object {
        private val ALEX_STYL = ContactFixture.aContactCalled("Alex Styl")
        private val MARIA_PAPADOPOULOU = ContactFixture.aContactCalled("Maria Papadopoulou")
        private val MIMOZA = ContactFixture.aContactCalled("Mimoza Dereks")
        private val aDate = dateOn(1, JANUARY, 2018)
    }
}
