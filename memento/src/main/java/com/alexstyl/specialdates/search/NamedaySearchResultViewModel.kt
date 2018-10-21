package com.alexstyl.specialdates.search

import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.events.namedays.NameCelebrations

data class NamedaySearchResultViewModel(val namedays: NameCelebrations) : SearchResultViewModel {

    val name: String
        get() = namedays.name

    fun getDate(i: Int): Date {
        return namedays.dates[i]
    }

    fun size(): Int {
        return namedays.dates.size
    }
}
