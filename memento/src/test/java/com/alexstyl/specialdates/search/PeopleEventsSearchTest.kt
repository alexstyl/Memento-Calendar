package com.alexstyl.specialdates.search

import com.alexstyl.specialdates.TestContactEventsBuilder
import com.alexstyl.specialdates.contact.ContactFixture
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.TimePeriod
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsProvider
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner
import java.util.ArrayList

@RunWith(MockitoJUnitRunner::class)
class PeopleEventsSearchTest {

    private lateinit var search: PeopleEventsSearch
    @Mock
    private lateinit var mockProvider: PeopleEventsProvider

    @Before
    fun setUp() {
        search = PeopleEventsSearch(mockProvider, NameMatcher.INSTANCE)
        val contactEvents = TestContactEventsBuilder()
                .addBirthdayFor(ALEX, JANUARY_1st)
                .addAnniversaryFor(MARIA, JANUARY_1st)
                .addNamedayFor(MIMOZA, JANUARY_1st)
                .build()

        given(mockProvider.fetchEventsBetween(aYearFromNow())).willReturn(contactEvents)
    }

    @Test
    fun searchingByFirstLetter() {
        val actual = search.searchForContacts("A", 5)
        val expected = ContactWithEvents(ALEX, listOf(ContactEventTestBuilder(ALEX).buildBirthday(JANUARY_1st)))

        assertThat(actual).containsOnly(expected)
    }

    @Test
    fun searchingByLastLetter() {
        val actual = search.searchForContacts("S", 5)
        val expected = ContactWithEvents(ALEX, listOf(ContactEventTestBuilder(ALEX).buildBirthday(JANUARY_1st)))

        assertThat(actual).containsOnly(expected)
    }

    @Test
    fun searchingByFullName() {
        val actual = search.searchForContacts("Alex Styl", 5)
        val expected = ContactWithEvents(ALEX, listOf(ContactEventTestBuilder(ALEX).buildBirthday(JANUARY_1st)))

        assertThat(actual).containsOnly(expected)
    }

    @Test
    fun multipleResults() {
        val actual = search.searchForContacts("M", 5)

        val expected = ArrayList<ContactWithEvents>()
        expected.add(ContactWithEvents(MIMOZA, ContactEventTestBuilder(MIMOZA).buildNameday(JANUARY_1st)))
        expected.add(ContactWithEvents(MARIA, ContactEventTestBuilder(MARIA).buildAnniversary(JANUARY_1st)))

        assertThat(actual).containsAll(expected)
    }

    @Test
    fun requestOneResultReturnsOnlyOneResult() {
        val actual = search.searchForContacts("M", 1)

        val expected = ArrayList<ContactWithEvents>()
        expected.add(ContactWithEvents(MARIA, ContactEventTestBuilder(MARIA).buildAnniversary(JANUARY_1st)))

        assertThat(actual).containsAll(expected)
    }

    companion object {

        private val ALEX = ContactFixture.aContactCalled("Alex Styl")
        private val MARIA = ContactFixture.aContactCalled("Maria Papadopoulou")
        private val MIMOZA = ContactFixture.aContactCalled("Mimoza Dereks")
        private val JANUARY_1st = Date.startOfYear(2016)

        private fun aYearFromNow(): TimePeriod {
            val today = Date.today()
            val aYearFromNow = today.addDay(364)
            return TimePeriod.between(today, aYearFromNow)
        }
    }
}
