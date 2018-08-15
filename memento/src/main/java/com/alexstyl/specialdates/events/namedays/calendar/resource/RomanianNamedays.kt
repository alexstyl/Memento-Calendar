package com.alexstyl.specialdates.events.namedays.calendar.resource

import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.events.namedays.NameCelebrations
import com.alexstyl.specialdates.events.namedays.NamedayBundle
import com.alexstyl.specialdates.events.namedays.NamedaysList
import com.alexstyl.specialdates.events.namedays.NamesInADate
import com.alexstyl.specialdates.events.namedays.NoNamesInADate

data class RomanianNamedays(private val calculator: RomanianEasterSpecialCalculator, private val names: List<String>) {

    private var namedays: NamedayBundle? = null
    private var romanianDate: Date? = null

    val allNames: ArrayList<String>
        get() = ArrayList(names)

    fun getNamedaysFor(date: Date): NamesInADate {
        calculateEasterIfNecessary(date.year!!)
        return if (romanianDate == date) {
            namedays!!.getNamedaysFor(date)
        } else NoNamesInADate(date)
    }

    fun getNamedaysFor(name: String, year: Int): NameCelebrations {
        calculateEasterIfNecessary(year)
        return namedays!!.getDatesFor(name)
    }

    private fun calculateEasterIfNecessary(year: Int) {
        if (romanianDate == null || romanianDate!!.year != year) {
            romanianDate = calculator.calculateSpecialRomanianDayForYear(year)

            val dateToNames = NamedaysList()
            val namesToDate = CharacterNode()
            for (name in names) {
                dateToNames.addNameday(romanianDate!!, name)
                namesToDate.addDate(name, romanianDate!!)
            }

            namedays = NamedayBundle(namesToDate, dateToNames)
        }
    }
}
