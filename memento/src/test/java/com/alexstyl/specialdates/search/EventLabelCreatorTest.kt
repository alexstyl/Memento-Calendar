package com.alexstyl.specialdates.search

import com.alexstyl.specialdates.JavaStrings
import com.alexstyl.specialdates.TestDateLabelCreator
import com.alexstyl.specialdates.contact.ContactFixture
import com.alexstyl.specialdates.date.ContactEvent
import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.Months.DECEMBER
import com.alexstyl.specialdates.date.dateOn
import com.alexstyl.specialdates.date.todaysDate
import com.alexstyl.specialdates.events.peopleevents.CustomEventType
import com.alexstyl.specialdates.events.peopleevents.EventType
import com.alexstyl.specialdates.events.peopleevents.StandardEventType.ANNIVERSARY
import com.alexstyl.specialdates.events.peopleevents.StandardEventType.BIRTHDAY
import com.alexstyl.specialdates.events.peopleevents.StandardEventType.NAMEDAY
import com.alexstyl.specialdates.events.peopleevents.StandardEventType.OTHER
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class EventLabelCreatorTest {

    private var creator: ContactEventLabelCreator? = null
    private val mockContact = ContactFixture.aContact()

    @Before
    fun setUp() {
        creator = ContactEventLabelCreator(todaysDate(), JavaStrings(), TestDateLabelCreator.forUS())
    }

    @Test
    fun birthdayWithoutYearIsCalculatedCorrectly() {
        val date = dateOn(12, DECEMBER)
        val event = contactEventOn(date, BIRTHDAY)
        val label = creator!!.createFor(event)

        assertThat(label).isEqualTo("Birthday on December 12")
    }

    @Test
    fun birthdayWithYearIsCalculatedCorrectly() {
        val age = Date.CURRENT_YEAR - 1990
        val date = dateOn(12, DECEMBER, 1990)

        val event = contactEventOn(date, BIRTHDAY)
        val label = creator!!.createFor(event)

        assertThat(label).isEqualTo("Turns $age on December 12")
    }

    @Test
    fun namedayIsCalculatedCorrectly() {
        val date = dateOn(12, DECEMBER)
        val event = contactEventOn(date, NAMEDAY)
        val label = creator!!.createFor(event)

        assertThat(label).isEqualTo("Nameday on December 12")
    }

    @Test
    fun anniversaryIsCalculatedCorrectly() {
        val date = dateOn(12, DECEMBER)
        val event = contactEventOn(date, ANNIVERSARY)
        val label = creator!!.createFor(event)

        assertThat(label).isEqualTo("Anniversary on December 12")
    }

    @Test
    fun otherIsCalculatedCorrectly() {
        val date = dateOn(12, DECEMBER)
        val event = contactEventOn(date, OTHER)
        val label = creator!!.createFor(event)

        assertThat(label).isEqualTo("Other on December 12")
    }

    @Test
    fun customIsCalculatedCorrectly() {
        val date = dateOn(12, DECEMBER)
        val event = contactEventOn(date, CustomEventType("H4x"))
        val label = creator!!.createFor(event)

        assertThat(label).isEqualTo("H4x on December 12")
    }

    private fun contactEventOn(date: Date, eventType: EventType): ContactEvent {
        return ContactEvent(eventType, date, mockContact)
    }
}
