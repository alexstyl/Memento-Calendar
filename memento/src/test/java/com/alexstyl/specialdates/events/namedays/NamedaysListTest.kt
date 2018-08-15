package com.alexstyl.specialdates.events.namedays

import com.alexstyl.specialdates.date.Months
import com.alexstyl.specialdates.date.dateOn
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class NamedaysListTest {

    @Before
    fun setUp() {
        populateNamedays()
    }

    @Test
    fun whenNoYearSpecified_thenRecurringEventIsReturned() {
        val dateWithNoYear = dateOn(4, FIXED_MONTH)

        val results = namedays.getNamedaysFor(dateWithNoYear)

        assertThat(results.names[0]).isEqualTo(RECURRING_NAMEDAY)
    }

    @Test
    fun whenNoYearSpecified_thenFixedYearEventIsNotReturned() {
        val dateWithNoYear = dateOn(1, FIXED_MONTH)

        val results = namedays.getNamedaysFor(dateWithNoYear)

        assertThat(results.names).isEmpty()
    }

    @Test
    fun whenYearSpecified_thenFixedYearEventIsReturned() {
        val dateWithNoYear = dateOn(1, FIXED_MONTH, FIXED_YEAR)

        val results = namedays.getNamedaysFor(dateWithNoYear)

        assertThat(results.names[0]).isEqualTo(FIXED_YEAR_NAMEDAY)
    }

    @Test
    fun whenYearSpecified_thenRecurringEventIsReturned() {
        val date = dateOn(4, FIXED_MONTH, FIXED_YEAR)

        val results = namedays.getNamedaysFor(date)

        assertThat(results.names[0]).isEqualTo(RECURRING_NAMEDAY)
    }

    companion object {

        private const val RECURRING_NAMEDAY = "recurring_nameday"
        private const val FIXED_YEAR_NAMEDAY = "fixed_date_nameday"
        private const val FIXED_YEAR = 2015
        private const val FIXED_MONTH = Months.JANUARY

        private val namedays = NamedaysList()

        private fun populateNamedays() {
            namedays.addNameday(dateOn(1, FIXED_MONTH, FIXED_YEAR), FIXED_YEAR_NAMEDAY)
            namedays.addNameday(dateOn(2, FIXED_MONTH, FIXED_YEAR), FIXED_YEAR_NAMEDAY)
            namedays.addNameday(dateOn(3, FIXED_MONTH, FIXED_YEAR), FIXED_YEAR_NAMEDAY)
            namedays.addNameday(dateOn(4, FIXED_MONTH), RECURRING_NAMEDAY)
            namedays.addNameday(dateOn(5, FIXED_MONTH), RECURRING_NAMEDAY)

        }
    }

}
