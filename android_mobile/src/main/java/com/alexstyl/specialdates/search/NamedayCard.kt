package com.alexstyl.specialdates.search

import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.events.namedays.NameCelebrations

class NamedayCard {

    private var namedays: NameCelebrations? = null

    val isAvailable: Boolean
        get() = namedays != null && namedays!!.dates.isNotEmpty()

    val name: String
        get() = namedays!!.name

    fun setNameday(nameday: NameCelebrations) {
        this.namedays = nameday
    }

    fun getDate(i: Int): Date {
        return namedays!!.dates[i]
    }

    fun clear() {
        this.namedays = null
    }

    fun size(): Int {
        return namedays!!.dates.size
    }
}
