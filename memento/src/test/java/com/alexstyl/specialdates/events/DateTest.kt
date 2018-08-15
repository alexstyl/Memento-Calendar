package com.alexstyl.specialdates.events

import com.alexstyl.specialdates.date.DateComparator
import com.alexstyl.specialdates.date.MonthInt
import com.alexstyl.specialdates.date.Months.APRIL
import com.alexstyl.specialdates.date.Months.DECEMBER
import com.alexstyl.specialdates.date.Months.FEBRUARY
import com.alexstyl.specialdates.date.Months.JANUARY
import com.alexstyl.specialdates.date.dateOn
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test

class DateTest {

    @Test
    fun whenAddingADayOfMonth_thenIncreaseNormally() {

        val after = ANY_DATE.addDay(1)
        val expectedDay = DAY + 1

        assertThat(after.dayOfMonth).isEqualTo(expectedDay)
    }

    @Test
    fun givenAEndOfTheYearDate_whenAddingOneDay_thenTheFirstDayOfTheNextYearIsReturned() {
        val lastDayOfYear = dateOn(31, DECEMBER, 1990)
        val firstDayOfNextYear = lastDayOfYear.addDay(1)

        assertThat(firstDayOfNextYear.month).isEqualTo(1)
        assertThat(firstDayOfNextYear.dayOfMonth).isEqualTo(1)
        val nextYear = lastDayOfYear.year!! + 1
        assertThat(firstDayOfNextYear.year).isEqualTo(nextYear)
    }

    @Test
    fun givenDateWithShortMonthAndNoYearSpecified_thenReturn29Days() {
        val date = dateOn(1, FEBRUARY)

        assertThat(date.daysInCurrentMonth).isEqualTo(29)
    }

    @Test
    fun givenDateWithShortMonthAndCommonYear_thenReturn28Days() {
        val date = dateOn(1, FEBRUARY, 2018)

        assertThat(date.daysInCurrentMonth).isEqualTo(28)
    }

    @Test
    fun givenDateWithShortMonthAndLeapYear_thenReturn29Days() {
        val date = dateOn(1, FEBRUARY, 2020)

        assertThat(date.daysInCurrentMonth).isEqualTo(29)
    }

    @Test
    fun testOneDayAhead() {
        val firstDayOfYear = dateOn(1, JANUARY, 1990)
        val secondDayOfYear = dateOn(2, JANUARY, 1990)
        assertThat(firstDayOfYear.daysDifferenceTo(secondDayOfYear)).isEqualTo(1)
    }

    @Test
    fun testOneYearAhead() {
        val firstDayOfYear = dateOn(1, JANUARY, 1990)
        val secondDayOfYear = dateOn(1, JANUARY, 1991)
        assertThat(firstDayOfYear.daysDifferenceTo(secondDayOfYear)).isEqualTo(365)
    }

    @Test
    fun testTwoYearAhead() {
        val firstDayOfYear = dateOn(1, JANUARY, 1990)
        val secondDayOfYear = dateOn(1, JANUARY, 1992)
        assertThat(firstDayOfYear.daysDifferenceTo(secondDayOfYear)).isEqualTo(365 * 2)
    }

    @Test
    fun compareFutureDayDate() {
        val result = comparator.compare(dateOn(1, JANUARY, 1990), dateOn(2, JANUARY, 1990))
        assertThat(result).isNegative
    }

    @Test
    fun compareSameDayDate() {
        val result = comparator.compare(dateOn(1, JANUARY, 1990), dateOn(1, JANUARY, 1990))
        assertThat(result).isZero
    }

    @Test
    fun comparePastDayDate() {
        val result = comparator.compare(dateOn(1, JANUARY, 1990), dateOn(1, JANUARY, 1980))
        assertThat(result).isPositive
    }

    @Test
    fun compareFutureDate() {
        val result = comparator.compare(dateOn(1, JANUARY, 1990), dateOn(2, JANUARY))
        assertThat(result).isNegative
    }

    @Test
    fun compareSameDate() {
        val result = comparator.compare(dateOn(1, JANUARY, 1990), dateOn(1, JANUARY))
        assertThat(result).isZero
    }

    @Test
    fun comparePastDate() {
        val result = comparator.compare(dateOn(2, JANUARY, 1990), dateOn(1, JANUARY))
        assertThat(result).isPositive
    }

    @Test
    fun whenComparingToSameDate_thenTheyAreEqual() {
        val firstDate = dateOn(16, APRIL, 1991)
        val secondDate = dateOn(16, APRIL, 1991)

        assertThat(firstDate == secondDate).isTrue
    }

    @Test
    fun whenComparingToDateWithDifferentYear_thenTheyAreNotEqual() {
        val firstDate = dateOn(16, APRIL, 1991)
        val secondDate = dateOn(16, APRIL, 1987)

        assertThat(firstDate == secondDate).isFalse
    }

    @Test(expected = IllegalArgumentException::class)
    fun throwsException_whenInvalidDateIsCreated() {
        dateOn(31, FEBRUARY, 1991)
    }

    companion object {

        private val comparator = DateComparator.INSTANCE

        private val DAY = 5
        @MonthInt
        private val MONTH = 10

        private val ANY_DATE = dateOn(DAY, MONTH, 1990)
    }
}
