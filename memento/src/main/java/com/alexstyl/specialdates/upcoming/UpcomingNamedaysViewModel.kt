package com.alexstyl.specialdates.upcoming

import com.alexstyl.specialdates.date.Date

data class UpcomingNamedaysViewModel(val namesLabel: String, val date: Date) : UpcomingRowViewModel {

    override val viewType: Int
        get() = UpcomingRowViewType.NAMEDAY_CARD

    override val id: Long
        get() = namesLabel.hashCode().toLong()
}
