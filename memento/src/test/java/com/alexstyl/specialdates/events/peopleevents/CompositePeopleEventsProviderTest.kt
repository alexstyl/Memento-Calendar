package com.alexstyl.specialdates.events.peopleevents

import com.alexstyl.specialdates.TestContactEventsBuilder
import com.alexstyl.specialdates.contact.ContactFixture
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.Months.DECEMBER
import com.alexstyl.specialdates.date.Months.JANUARY
import com.alexstyl.specialdates.date.Months.MARCH
import com.alexstyl.specialdates.date.dateOn
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CompositePeopleEventsProviderTest {

    @Mock
    private lateinit var mockDeviceEvents: PeopleEventsProvider
    @Mock
    private lateinit var mockPeopleDynamicNamedaysProvider: PeopleDynamicNamedaysProvider

    private lateinit var peopleEventsProvider: CompositePeopleEventsProvider

    @Before
    fun setUp() {
        peopleEventsProvider = CompositePeopleEventsProvider(
                listOf(mockDeviceEvents, mockPeopleDynamicNamedaysProvider)
        )
    }

    @Test
    fun whenOnlyDeviceEventsExist_willReturnsOnlyThoseEvents() {
        val date = dateOn(1, JANUARY, 2017)
        val expectedEvents = TestContactEventsBuilder().addAnniversaryFor(PETER, date).build()

        given(mockDeviceEvents.fetchEventsOn(date)).willReturn(ContactEventsOnADate.createFrom(date, expectedEvents))
        mockPeopleDynamicNamedaysProvider.willReturnNoEventsOn(date)

        val events = peopleEventsProvider.fetchEventsOn(date)
        assertThat(events.events).containsOnly(expectedEvents[0])
    }

    @Test
    fun whenOnlyNamedaysExist_willReturnsOnlyThoseEvents() {
        val theDate = dateOn(1, JANUARY, 2017)

        val expectedEvents = ContactEventsOnADate.createFrom(theDate, TestContactEventsBuilder().addNamedayFor(PETER, theDate).build())
        given(mockPeopleDynamicNamedaysProvider.fetchEventsOn(theDate)).willReturn(expectedEvents)
        mockDeviceEvents.willReturnNoEventsOn(theDate)

        val actualEventsOnADate = peopleEventsProvider.fetchEventsOn(theDate)
        assertThat(actualEventsOnADate).isEqualTo(expectedEvents)
    }

    @Test
    fun whenBothDeviceAndNamedaysEventsExist_thenAllEventsAreReturnedCorrectly() {
        val date = dateOn(1, JANUARY, 2017)
        val expectedDynamicEvents = TestContactEventsBuilder().addNamedayFor(PETER, date).build()
        val expectedStaticEvents = TestContactEventsBuilder().addAnniversaryFor(PETER, date).build()

        given(mockPeopleDynamicNamedaysProvider.fetchEventsOn(date)).willReturn(ContactEventsOnADate.createFrom(date, expectedDynamicEvents))
        given(mockDeviceEvents.fetchEventsOn(date)).willReturn(ContactEventsOnADate.createFrom(date, expectedStaticEvents))

        val events = peopleEventsProvider.fetchEventsOn(date)
        assertThat(events.events).containsAll(expectedDynamicEvents)
        assertThat(events.events).containsAll(expectedStaticEvents)
    }

    @Test(expected = NoEventsFoundException::class)
    @Throws(NoEventsFoundException::class)
    fun whenNoEventsExist_thenThrowsException() {
        val aDate = dateOn(1, JANUARY, 2017)

        mockDeviceEvents.willReturnNoEventsOn(aDate)
        mockPeopleDynamicNamedaysProvider.willReturnNoEventsOn(aDate)

        peopleEventsProvider.findClosestEventDateOnOrAfter(aDate)
    }

    @Test
    fun onlyDynamicEvents_returnsTheDynamicEvents() {
        val aDate = dateOn(2, MARCH, 2017)

        given(mockDeviceEvents.findClosestEventDateOnOrAfter(aDate)).willReturn(null)
        given(mockDeviceEvents.fetchEventsOn(aDate)).willReturn(ContactEventsOnADate.createFrom(aDate, emptyList()))

        given(mockPeopleDynamicNamedaysProvider.findClosestEventDateOnOrAfter(aDate)).willReturn(aDate)

        val expectedEvents = TestContactEventsBuilder()
                .addNamedayFor(PETER, aDate)
                .build()
        given(mockPeopleDynamicNamedaysProvider.fetchEventsOn(aDate)).willReturn(ContactEventsOnADate.createFrom(aDate, expectedEvents))

        val actualEvents = peopleEventsProvider.findClosestEventDateOnOrAfter(aDate)
        val events = peopleEventsProvider.fetchEventsOn(actualEvents!!)
        assertThat(events).isEqualTo(ContactEventsOnADate.createFrom(aDate, expectedEvents))
    }

    companion object {
        private val PETER = ContactFixture.aContactCalled("Peter")
    }

    @Test
    fun findClosestEventDateOnOrAfterReturnsTheClosest() {
        val date = dateOn(5, JANUARY, 2018)

        given(mockDeviceEvents.findClosestEventDateOnOrAfter(date)).willReturn(dateOn(29, DECEMBER, 2018))
        given(mockPeopleDynamicNamedaysProvider.findClosestEventDateOnOrAfter(date)).willReturn(dateOn(6, JANUARY,2018))

        assertThat(peopleEventsProvider.findClosestEventDateOnOrAfter(date)).isEqualTo(dateOn(6, JANUARY, 2018))
    }
}


private fun PeopleEventsProvider.willReturnNoEventsOn(date: Date) {
    given(fetchEventsOn(date)).willReturn(ContactEventsOnADate.createFrom(date, emptyList()))
    given(findClosestEventDateOnOrAfter(date)).willThrow(NoEventsFoundException::class.java)
}
