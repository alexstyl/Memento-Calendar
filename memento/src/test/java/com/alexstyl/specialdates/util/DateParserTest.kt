package com.alexstyl.specialdates.util

import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.DateParser
import com.alexstyl.specialdates.date.Months.FEBRUARY
import com.alexstyl.specialdates.date.Months.JANUARY
import com.alexstyl.specialdates.date.Months.MARCH
import com.alexstyl.specialdates.date.Months.OCTOBER
import com.alexstyl.specialdates.facebook.friendimport.SystemLogTracker
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test

class DateParserTest {

    private val dateParser = DateParser(SystemLogTracker())

    @Test
    fun dateWithSlashes() {
        val dateDashes = "13/Jan/1972"
        val parsed = dateParser.parse(dateDashes)
        assertThat(parsed).isEqualTo(Date.on(13, JANUARY, 1972))
    }

    @Test
    fun longDate() {
        val dateDashes = "1949-02-14T00:00:00Z"
        val parsed = dateParser.parse(dateDashes)
        assertThat(parsed).isEqualTo(Date.on(14, FEBRUARY, 1949))
    }

    @Test
    fun longDate2() {
        val dateDashes = "20151026T083936Z"
        val parsed = dateParser.parse(dateDashes)
        assertThat(parsed).isEqualTo(Date.on(26, OCTOBER, 2015))
    }

    @Test
    fun datesWithDashes() {
        val dateDashes = "2016-03-29"
        val parsed = dateParser.parse(dateDashes)
        assertThat(parsed).isEqualTo(Date.on(29, MARCH, 2016))
    }

    @Test
    fun datesWithDashesWithoutYear() {
        val dateDashes = "--03-29"
        val parsed = dateParser.parse(dateDashes)
        assertThat(parsed).isEqualTo(Date.on(29, MARCH))
    }

    @Test
    fun noYear_on_29th_Of_February() {
        val dateDashes = "--02-29"
        val parsed = dateParser.parse(dateDashes)
        assertThat(parsed).isEqualTo(Date.on(29, FEBRUARY))
    }

    @Test(expected = NullPointerException::class)
    fun throwsExceptionWhenNullIsPassed() {
        dateParser.parse(null!!)
    }
}
