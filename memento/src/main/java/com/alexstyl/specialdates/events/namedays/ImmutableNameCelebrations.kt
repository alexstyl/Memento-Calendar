package com.alexstyl.specialdates.events.namedays

import com.alexstyl.specialdates.date.Date

data class ImmutableNameCelebrations(override val name: String, override val dates: List<Date>) : NameCelebrations {

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
