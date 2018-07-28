package com.alexstyl.specialdates.events.namedays

import com.alexstyl.specialdates.date.Date

/**
 * A name and the list of Namedays it's celebrated
 */
interface NameCelebrations {
    val name: String
    val dates: List<Date>

    operator fun plus(other: NameCelebrations): NameCelebrations {
        return ImmutableNameCelebrations(name, this.dates + other.dates)
    }
}

