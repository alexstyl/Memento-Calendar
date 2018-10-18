package com.alexstyl.specialdates.search

import com.alexstyl.specialdates.TestContactEventsBuilder
import com.alexstyl.specialdates.contact.ContactFixture
import com.alexstyl.specialdates.date.TimePeriod
import com.alexstyl.specialdates.date.beggingOfYear
import com.alexstyl.specialdates.date.todaysDate
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsProvider
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
    private lateinit var mockProvider: PeopleEventsProvider

    @Before
    fun setUp() {
        search = PeopleEventsSearch(mockProvider, NameMatcher)
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
        val expected = ContactWithEvents(ALEX, TestContactEventsBuilder().addBirthdayFor(ALEX, JANUARY_1st).build())

        assertThat(actual).containsOnly(expected)
    }

    @Test
    fun searchingByLastLetter() {
        val actual = search.searchForContacts("S", 5)
        val expected = ContactWithEvents(ALEX, TestContactEventsBuilder().addBirthdayFor(ALEX, JANUARY_1st).build())

        assertThat(actual).containsOnly(expected)
    }

    @Test
    fun searchingByFullName() {
        val actual = search.searchForContacts("Alex Styl", 5)
        val expected = ContactWithEvents(ALEX, TestContactEventsBuilder().addBirthdayFor(ALEX, JANUARY_1st).build())

        assertThat(actual).containsOnly(expected)
    }

    @Test
    fun multipleResults() {
        val actual = search.searchForContacts("M", 5)

        val expected = listOf(
                ContactWithEvents(MIMOZA, TestContactEventsBuilder().addNamedayFor(MIMOZA, JANUARY_1st).build()),
                ContactWithEvents(MARIA, TestContactEventsBuilder().addAnniversaryFor(MARIA, JANUARY_1st).build())
        )

        assertThat(actual).containsAll(expected)
    }

    @Test
    fun requestOneResultReturnsOnlyOneResult() {
        val actual = search.searchForContacts("M", 1)

        val expected = listOf(
                ContactWithEvents(MARIA, TestContactEventsBuilder().addAnniversaryFor(MARIA, JANUARY_1st).build())
        )

        assertThat(actual).containsAll(expected)
    }

    companion object {

        private val ALEX = ContactFixture.aContactCalled("Alex Styl")
        private val MARIA = ContactFixture.aContactCalled("Maria Papadopoulou")
        private val MIMOZA = ContactFixture.aContactCalled("Mimoza Dereks")
        private val JANUARY_1st = beggingOfYear(2016)

        private fun aYearFromNow(): TimePeriod {
            val today = todaysDate()
            val aYearFromNow = today.addDay(364)
            return TimePeriod.between(today, aYearFromNow)
        }
    }
}
