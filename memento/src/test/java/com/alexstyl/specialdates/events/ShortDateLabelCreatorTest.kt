package com.alexstyl.specialdates.events

import com.alexstyl.specialdates.date.Months.MAY
import com.alexstyl.specialdates.date.dateOn
import com.alexstyl.specialdates.events.peopleevents.ShortDateLabelCreator
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test

class ShortDateLabelCreatorTest {

    @Test
    fun givenDateWithYear_whenAskingYear_thenYearIsReturned() {
        val date = dateOn(5, MAY, 1995)
        val dateToString = CREATOR.createLabelWithYearPreferredFor(date)
        assertThat(dateToString).isEqualTo("1995-05-05")
    }

    @Test
    fun givenDateWithNoYear_whenAskingForYear_NoYearIsReturned() {
        val date = dateOn(5, MAY)
        val dateToString = CREATOR.createLabelWithYearPreferredFor(date)
        assertThat(dateToString).isEqualTo("--05-05")
    }

    @Test
    fun givenDateWithNoYear_whenAskingNoYear_thenNoYearIsReturned() {
        val date = dateOn(5, MAY)
        val dateToString = CREATOR.createLabelWithNoYearFor(date)
        assertThat(dateToString).isEqualTo("05-05")
    }

    @Test
    fun givenDateWithYear_whenAskingForNoYear_NoYearIsReturned() {
        val date = dateOn(5, MAY, 1990)
        val dateToString = CREATOR.createLabelWithNoYearFor(date)
        assertThat(dateToString).isEqualTo("05-05")
    }

    companion object {
        private val CREATOR = ShortDateLabelCreator()
    }
}
