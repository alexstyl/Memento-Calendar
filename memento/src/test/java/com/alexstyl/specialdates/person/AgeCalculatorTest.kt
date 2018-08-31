package com.alexstyl.specialdates.person

import com.alexstyl.specialdates.date.Months
import com.alexstyl.specialdates.date.dateOn
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AgeCalculatorTest {

    @Test
    fun birthdayWithoutYear_returnsNoAge() {
        val ageCalculator = AgeCalculator(dateOn(5, Months.MARCH, 2017))
        val ageOf = ageCalculator.ageOf(dateOn(4, Months.MARCH))
        assertThat(ageOf).isEqualTo("")
    }

    @Test
    fun birthdayBeforeToday() {
        val ageCalculator = AgeCalculator(dateOn(5, Months.MARCH, 2017))
        val ageOf = ageCalculator.ageOf(dateOn(4, Months.MARCH, 1990))
        assertThat(ageOf).isEqualTo("27")
    }

    @Test
    fun birthdayAfterToday() {
        val ageCalculator = AgeCalculator(dateOn(5, Months.MARCH, 2017))
        val ageOf = ageCalculator.ageOf(dateOn(8, Months.MARCH, 1990))
        assertThat(ageOf).isEqualTo("26")
    }

    @Test
    fun birthdayOnToday() {
        val ageCalculator = AgeCalculator(dateOn(18, Months.AUGUST, 2017))
        val ageOf = ageCalculator.ageOf(dateOn(18, Months.AUGUST, 2017))
        assertThat(ageOf).isEqualTo("")
    }

    @Test
    fun dateOfBirthOnFuture() {
        val ageCalculator = AgeCalculator(dateOn(17, Months.AUGUST, 2017))
        val ageOf = ageCalculator.ageOf(dateOn(18, Months.AUGUST, 2017))
        assertThat(ageOf).isEqualTo("")
    }
}
