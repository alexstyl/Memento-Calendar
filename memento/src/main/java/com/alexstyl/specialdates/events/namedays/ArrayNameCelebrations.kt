package com.alexstyl.specialdates.events.namedays

import com.alexstyl.specialdates.date.Date

data class ArrayNameCelebrations(override val name: String) : MutableNameCelebrations {

    override val dates: List<Date>
        get() = _dates

    private val _dates = mutableListOf<Date>()


    override fun addDate(date: Date) {
        _dates.add(date)
    }

    override fun equals(other: Any?): Boolean {
        if (other != null && other is NameCelebrations) {
            return this.name == other.name && this.dates == other.dates
        }
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

}
