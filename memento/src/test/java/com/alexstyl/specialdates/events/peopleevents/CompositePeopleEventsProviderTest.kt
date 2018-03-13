package com.alexstyl.specialdates.events.peopleevents

import com.alexstyl.specialdates.TestContactEventsBuilder
import com.alexstyl.specialdates.contact.ContactFixture
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.Months.JANUARY
import com.alexstyl.specialdates.date.Months.MARCH
import com.alexstyl.specialdates.events.namedays.NamedayUserSettings
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
    private lateinit var mockNamedaysPreferences: NamedayUserSettings
    @Mock
    private lateinit var mockDeviceEvents: PeopleEventsProvider
    @Mock
    private lateinit var mockPeopleNamedaysCalculator: PeopleNamedaysCalculator

    private lateinit var peopleEventsProvider: CompositePeopleEventsProvider

    @Before
    fun setUp() {
        peopleEventsProvider = CompositePeopleEventsProvider(
                mockNamedaysPreferences,
                mockPeopleNamedaysCalculator,
                mockDeviceEvents
        )
    }

    @Test
    fun whenOnlyDeviceEventsExist_willReturnsOnlyThoseEvents() {
        val date = Date.on(1, JANUARY, 2017)
        val expectedEvents = TestContactEventsBuilder().addAnniversaryFor(PETER, date).build()

        given(mockDeviceEvents.fetchEventsOn(date)).willReturn(ContactEventsOnADate.createFrom(date, expectedEvents))
        mockPeopleNamedaysCalculator.willReturnNoEventsOn(date)

        val events = peopleEventsProvider.fetchEventsOn(date)
        assertThat(events.events).containsOnly(expectedEvents[0])
    }

    @Test
    fun whenOnlyNamedaysExist_willReturnsOnlyThoseEvents() {
        val date = Date.on(1, JANUARY, 2017)
        val expectedEvents = TestContactEventsBuilder().addNamedayFor(PETER, date).build()
        given(mockNamedaysPreferences.isEnabled).willReturn(true)
        given(mockPeopleNamedaysCalculator.fetchEventsOn(date)).willReturn(ContactEventsOnADate.createFrom(date, expectedEvents))
        mockDeviceEvents.willReturnNoEventsOn(date)

        val events = peopleEventsProvider.fetchEventsOn(date)
        assertThat(events.events).containsOnly(expectedEvents[0])
    }

    @Test
    fun whenBothDeviceAndNamedaysEventsExist_thenAllEventsAreReturnedCorrectly() {
        val date = Date.on(1, JANUARY, 2017)
        val expectedDynamicEvents = TestContactEventsBuilder().addNamedayFor(PETER, date).build()
        val expectedStaticEvents = TestContactEventsBuilder().addAnniversaryFor(PETER, date).build()

        given(mockNamedaysPreferences.isEnabled).willReturn(true)
        given(mockPeopleNamedaysCalculator.fetchEventsOn(date)).willReturn(ContactEventsOnADate.createFrom(date, expectedDynamicEvents))
        given(mockDeviceEvents.fetchEventsOn(date)).willReturn(ContactEventsOnADate.createFrom(date, expectedStaticEvents))

        val events = peopleEventsProvider.fetchEventsOn(date)
        assertThat(events.events).containsAll(expectedDynamicEvents)
        assertThat(events.events).containsAll(expectedStaticEvents)
    }

    @Test(expected = NoEventsFoundException::class)
    @Throws(NoEventsFoundException::class)
    fun whenNoEventsExist_thenThrowsException() {
        val aDate = Date.on(1, JANUARY, 2017)

        mockDeviceEvents.willReturnNoEventsOn(aDate)
        mockPeopleNamedaysCalculator.willReturnNoEventsOn(aDate)

        peopleEventsProvider.findClosestEventDateOnOrAfter(aDate)
    }

    @Test
    @Throws(NoEventsFoundException::class)
    fun onlyDynamicEvents_returnsTheDynamicEvents() {
        val aDate = Date.on(2, MARCH, 2017)

        given(mockDeviceEvents.findClosestEventDateOnOrAfter(aDate)).willThrow(NoEventsFoundException::class.java)
        given(mockDeviceEvents.fetchEventsOn(aDate)).willReturn(ContactEventsOnADate.createFrom(aDate, emptyList()))

        given(mockNamedaysPreferences.isEnabled).willReturn(true)
        given(mockPeopleNamedaysCalculator.findClosestEventDateOnOrAfter(aDate)).willReturn(aDate)

        val expectedEvents = TestContactEventsBuilder()
                .addNamedayFor(PETER, aDate)
                .build()
        given(mockPeopleNamedaysCalculator.fetchEventsOn(aDate)).willReturn(ContactEventsOnADate.createFrom(aDate, expectedEvents))

        val actualEvents = peopleEventsProvider.findClosestEventDateOnOrAfter(aDate)
        val events = peopleEventsProvider.fetchEventsOn(actualEvents)
        assertThat(events).isEqualTo(ContactEventsOnADate.createFrom(aDate, expectedEvents))
    }

    companion object {
        private val PETER = ContactFixture.aContactCalled("Peter")
    }

}

private fun PeopleEventsProvider.willReturnNoEventsOn(date: Date) {
    given(fetchEventsOn(date)).willReturn(ContactEventsOnADate.createFrom(date, emptyList()))
    given(findClosestEventDateOnOrAfter(date)).willThrow(NoEventsFoundException::class.java)
}
