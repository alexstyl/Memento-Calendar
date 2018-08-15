package com.alexstyl.specialdates.events.namedays.calendar.resource

import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.dateOn
import com.alexstyl.specialdates.events.namedays.calendar.OrthodoxEasterCalculator
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test

class RomanianEasterSpecialCalculatorTest {

    private var orthodoxEasterCalculator = OrthodoxEasterCalculator()

    @Test
    fun calculatesSundayBeforeEasterCorrectly() {
        val calculator = RomanianEasterSpecialCalculator(orthodoxEasterCalculator)
        val expectedDates = buildExpectedDates()
        for (expectedDate in expectedDates) {
            val actualDate = calculator.calculateSpecialRomanianDayForYear(expectedDate.year!!)
            assertThat(expectedDate).isEqualTo(actualDate)
        }
    }

    private fun buildExpectedDates(): List<Date> =
            listOf(dateOn(9, 4, 2017),
                    dateOn(1, 4, 2018),
                    dateOn(21, 4, 2019),
                    dateOn(12, 4, 2020),
                    dateOn(25, 4, 2021),
                    dateOn(17, 4, 2022))
}
