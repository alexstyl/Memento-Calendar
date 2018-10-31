package com.alexstyl.specialdates.events.namedays

import com.alexstyl.specialdates.date.Date

data class NoCelebrations(override val name: String) : NameCelebrations {
    override val dates: List<Date>
        get() = emptyList()

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
