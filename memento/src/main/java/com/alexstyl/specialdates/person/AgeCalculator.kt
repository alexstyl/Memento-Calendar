package com.alexstyl.specialdates.person

import com.alexstyl.specialdates.date.Date

class AgeCalculator(private val today: Date) {
    fun ageOf(dateOfBirth: Date): String {
        if (dateOfBirth.hasNoYear()) {
            // we cannot tell the age unless we know the year of birth
            return ""
        }

        val age: Int
        if (dateOfBirth.month > today.month || dateOfBirth.dayOfMonth > today.dayOfMonth) {
            age = yearsAfter(dateOfBirth) - 1
        } else {
            age = yearsAfter(dateOfBirth)
        }
        if (age <= 0) {
            return ""
        }
        return age.toString()
    }

    private fun yearsAfter(dateOfBirth: Date) = today.year!! - dateOfBirth.year!!

}
