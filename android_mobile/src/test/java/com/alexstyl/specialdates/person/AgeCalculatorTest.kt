package com.alexstyl.specialdates.person

import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.DateConstants
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AgeCalculatorTest {

    @Test
    fun birthdayWithoutYear_returnsNoAge() {
        val ageCalculator = AgeCalculator(Date.on(5, DateConstants.MARCH, 2017))
        val ageOf = ageCalculator.ageOf(Date.on(4, DateConstants.MARCH))
        assertThat(ageOf).isEqualTo("")
    }

    @Test
    fun birthdayBeforeToday() {
        val ageCalculator = AgeCalculator(Date.on(5, DateConstants.MARCH, 2017))
        val ageOf = ageCalculator.ageOf(Date.on(4, DateConstants.MARCH, 1990))
        assertThat(ageOf).isEqualTo("27")
    }

    @Test
    fun birthdayAfterToday() {
        val ageCalculator = AgeCalculator(Date.on(5, DateConstants.MARCH, 2017))
        val ageOf = ageCalculator.ageOf(Date.on(8, DateConstants.MARCH, 1990))


        assertThat(ageOf).isEqualTo("26")
    }
}
