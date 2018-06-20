package com.alexstyl.specialdates.events.namedays.calendar.resource

import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.Months.SUNDAY
import com.alexstyl.specialdates.events.namedays.calendar.OrthodoxEasterCalculator

class RomanianEasterSpecialCalculator(private val easterCalculator: OrthodoxEasterCalculator) {

    fun calculateSpecialRomanianDayForYear(year: Int): Date {
        var easter = easterCalculator.calculateEasterForYear(year)
        while (easter.dayOfWeek != SUNDAY) {
            easter = easter.addDay(-1)
        }
        return easter.addWeek(-1)
    }

}
