package com.alexstyl.specialdates.events.namedays

import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.dateOn

class MapNamedaysList : MutableNamedaysList {

    private val _names = mutableSetOf<String>()
    private val specificYearNamedays = mutableMapOf<Date, MutableNamesInADate>()

    private val recurringNamedays = mutableMapOf<Date, MutableNamesInADate>()

    override fun getNamedaysFor(date: Date): NamesInADate {
        assertHasYear(date)

        return (specificYearNamedays[date] ?: NoNamesInADate(date)) +
                (recurringNamedays[dateOn(date.dayOfMonth, date.month)] ?: NoNamesInADate(date))
    }

    override fun addSpecificYearNameday(date: Date, name: String) {
        assertHasYear(date)

        if (!specificYearNamedays.containsKey(date)) {
            specificYearNamedays[date] = ArrayNamesInADate(date, arrayListOf())
        }

        val specificYearNameday: MutableNamesInADate = specificYearNamedays[date]!!
        specificYearNameday.addName(name)
        _names.add(name)
    }

    private fun assertHasYear(date: Date) {
        if (date.hasNoYear()) {
            throw IllegalArgumentException("Must provide a date with a year to ask for Namedays")
        }
    }

    override fun addRecurringNameday(date: Date, name: String) {
        assertReccuringNamedayHasNoYear(date)

        if (!recurringNamedays.containsKey(date)) {
            recurringNamedays[date] = ArrayNamesInADate(date, arrayListOf())
        }

        val recurringNameday: MutableNamesInADate = recurringNamedays[date]!!
        recurringNameday.addName(name)
        _names.add(name)
    }

    private fun assertReccuringNamedayHasNoYear(date: Date) {
        if (date.hasYear()) {
            throw IllegalArgumentException("Recurring Namedays must have no year. Passed $date")
        }
    }

    override val names
        get() = _names.toMutableList()
}