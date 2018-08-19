package com.alexstyl.specialdates.events.namedays.calendar.resource

import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.events.namedays.MapNamedaysList
import com.alexstyl.specialdates.events.namedays.NameCelebrations
import com.alexstyl.specialdates.events.namedays.Namedays
import com.alexstyl.specialdates.events.namedays.NamesInADate
import com.alexstyl.specialdates.events.namedays.NoNamesInADate

data class RomanianNamedays(private val calculator: RomanianEasterSpecialCalculator, private val names: List<String>) {

    private var namedays: Namedays? = null
    private var easterDate: Date? = null


    val allNames: ArrayList<String>
        get() = ArrayList(names)

    fun getNamedaysFor(date: Date): NamesInADate {
        calculateEasterIfNecessary(date.year)
        return if (easterDate == date) {
            namedays!!.getNamedaysFor(date)
        } else NoNamesInADate(date)
    }

    fun getNamedaysFor(name: String, year: Int): NameCelebrations? {
        calculateEasterIfNecessary(year)
        return namedays!!.getDatesFor(name)
    }

    private fun calculateEasterIfNecessary(year: Int) {
        if (easterDate == null || easterDate!!.year != year) {
            easterDate = calculator.calculateSpecialRomanianDayForYear(year)

            val dateToNames = MapNamedaysList()
            val namesToDate = CharacterNode()
            for (name in names) {
                dateToNames.addSpecificYearNameday(easterDate!!, name)
                namesToDate.addDate(name, easterDate!!)
            }

            namedays = Namedays(namesToDate, dateToNames)
        }
    }
}
