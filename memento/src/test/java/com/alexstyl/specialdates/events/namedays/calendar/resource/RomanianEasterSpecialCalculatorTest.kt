package com.alexstyl.specialdates.events.namedays.calendar.resource

import com.alexstyl.specialdates.date.Date
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
            val actualDate = calculator.calculateSpecialRomanianDayForYear(expectedDate.year)
            assertThat(expectedDate).isEqualTo(actualDate)
        }
    }

    private fun buildExpectedDates(): List<Date> =
            listOf(Date.on(9, 4, 2017),
                    Date.on(1, 4, 2018),
                    Date.on(21, 4, 2019),
                    Date.on(12, 4, 2020),
                    Date.on(25, 4, 2021),
                    Date.on(17, 4, 2022))
}
