package com.alexstyl.specialdates.date

import com.alexstyl.specialdates.JavaStrings
import com.alexstyl.specialdates.Optional
import com.alexstyl.specialdates.contact.ContactFixture
import com.alexstyl.specialdates.date.Months.JANUARY
import com.alexstyl.specialdates.events.peopleevents.StandardEventType
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ContactEventTest {

    private val ANY_CONTACT = ContactFixture.aContact()

    private val strings = JavaStrings()

    @Test
    fun labelForNameday() {
        val contactEvent = ContactEvent(NO_DEVICE_ID, StandardEventType.NAMEDAY, SOME_DATE, ANY_CONTACT)
        val label = contactEvent.getLabel(SOME_DATE, strings)
        assertThat(label).isEqualTo("Nameday")
    }

    @Test
    fun labelForBirthdayWithoutYear() {
        val contactEvent = ContactEvent(NO_DEVICE_ID, StandardEventType.BIRTHDAY, SOME_DATE_WITHOUT_YEAR, ANY_CONTACT)
        val label = contactEvent.getLabel(SOME_DATE, strings)
        assertThat(label).isEqualTo("Birthday")
    }

    @Test
    fun labelForBirthdayWithYearAfterDate() {
        val eventDate = dateOn(1, JANUARY, CURRENT_YEAR + 50)
        val contactEvent = ContactEvent(NO_DEVICE_ID, StandardEventType.BIRTHDAY, eventDate, ANY_CONTACT)
        val label = contactEvent.getLabel(SOME_DATE, strings)
        assertThat(label).isEqualTo("Birthday")
    }

    @Test
    fun labelForBirthdayWithYearOnDate() {
        val eventDate = dateOn(1, JANUARY, CURRENT_YEAR)
        val contactEvent = ContactEvent(NO_DEVICE_ID, StandardEventType.BIRTHDAY, eventDate, ANY_CONTACT)
        val label = contactEvent.getLabel(SOME_DATE, strings)
        assertThat(label).isEqualTo("Birthday")
    }

    @Test
    fun labelForBirthdayWithYearBeforeDate() {
        val eventDate = dateOn(1, JANUARY, CURRENT_YEAR - 10)

        val contactEvent = ContactEvent(NO_DEVICE_ID, StandardEventType.BIRTHDAY, eventDate, ANY_CONTACT)
        val label = contactEvent.getLabel(dateOn(1, JANUARY, todaysDate().year), strings)
        assertThat(label).isEqualTo("Turns 10")
    }

    companion object {

        private val NO_DEVICE_ID = Optional.absent<Long>()
        private val SOME_DATE = dateOn(1, JANUARY, 1990)
        private val SOME_DATE_WITHOUT_YEAR = dateOn(1, JANUARY)
        private val CURRENT_YEAR = Date.CURRENT_YEAR
    }

}
