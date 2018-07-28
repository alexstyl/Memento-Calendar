package com.alexstyl.specialdates.events.namedays

import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.Dates

/**
 * A name and the list of Namedays it's celebrated
 */
data class NameCelebrations(val name: String, val dates: Dates) {

    constructor(name: String) : this(name, Dates(Dates()))

    constructor(name: String, easter: Date) : this(name, Dates(easter))

    fun containsNoDate(): Boolean = dates.containsNoDate()

    val isEmpty: Boolean
        get() = dates.size() == 0

    fun getDate(i: Int): Date = dates.getDate(i)

    fun size(): Int = dates.size()

    fun addDate(date: Date) {
        this.dates.add(date)
    }
}
