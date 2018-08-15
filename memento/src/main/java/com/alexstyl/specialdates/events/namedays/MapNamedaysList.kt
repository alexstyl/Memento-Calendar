package com.alexstyl.specialdates.events.namedays

import com.alexstyl.specialdates.date.Date
import java.text.Collator
import java.util.*

class MapNamedaysList : NamedaysList {

    private val _names_NOYEAR = TreeSet<String>(Collator.getInstance())
    private val namedaysNO_YEAR = mutableMapOf<Date, MutableNamesInADate>()

    private val _names_YEAR = TreeSet<String>(Collator.getInstance())
    private val namedays_YEAR = mutableMapOf<Date, MutableNamesInADate>()

    override fun getNamedaysFor(date: Date): NamesInADate {
        if (namedaysNO_YEAR[date] != null) {
            return namedaysNO_YEAR[date]!!
        }
        if (namedays_YEAR[date] != null) {
            return namedays_YEAR[date]!!
        }
        return NoNamesInADate(date)
    }

    override fun addNameday(date: Date, name: String) {
        if (date.hasYear()) {
            addNamedayYEAR(date, name)
        } else {
            addNamedayNO_YEAR(date, name)
        }
    }

    private fun addNamedayNO_YEAR(key: Date, name: String) {
        if (!namedaysNO_YEAR.containsKey(key)) {
            namedaysNO_YEAR[key] = ArrayNamesInADate(key, ArrayList())
        }
        val names = namedaysNO_YEAR[key]!!
        names.addName(name)
        this._names_NOYEAR.add(name)
    }

    private fun addNamedayYEAR(key: Date, name: String) {
        if (!namedays_YEAR.containsKey(key)) {
            namedays_YEAR[key] = ArrayNamesInADate(key, ArrayList())
        }
        val names = namedays_YEAR[key]!!
        names.addName(name)
        this._names_YEAR.add(name)
    }

    override val names
        get() = (_names_NOYEAR + _names_YEAR).toMutableList()
}