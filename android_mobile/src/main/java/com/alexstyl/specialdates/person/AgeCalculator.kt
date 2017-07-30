package com.alexstyl.specialdates.person

import com.alexstyl.specialdates.date.Date

class AgeCalculator(private val today: Date) {
    fun ageOf(birthday: Date): String {
        if (birthday.hasNoYear()) {
            return ""
        }

        var age = today.year - birthday.year
        if (birthday.month > today.month || birthday.dayOfMonth > today.dayOfMonth) {
            age--
        }
        return age.toString()
    }

}
