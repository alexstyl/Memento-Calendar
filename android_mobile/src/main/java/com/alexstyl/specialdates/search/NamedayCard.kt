package com.alexstyl.specialdates.search

import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.events.namedays.NameCelebrations
import com.alexstyl.specialdates.events.namedays.NoNameCelebrations

class NamedayCard {

    private var namedays: NameCelebrations = NoNameCelebrations("")

    val isAvailable: Boolean
        get() = namedays.dates.isNotEmpty()

    val name: String
        get() = namedays.name

    fun setNameday(nameday: NameCelebrations) {
        this.namedays = nameday
    }

    fun getDate(i: Int): Date {
        return namedays.dates[i]
    }

    fun clear() {
        this.namedays = NoNameCelebrations("")
    }

    fun size(): Int {
        return namedays.dates.size
    }
}
