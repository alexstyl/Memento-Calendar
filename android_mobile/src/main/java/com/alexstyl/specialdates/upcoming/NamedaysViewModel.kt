package com.alexstyl.specialdates.upcoming

internal class NamedaysViewModel(val namesLabel: String) : UpcomingRowViewModel {

    override val viewType: Int
        get() = UpcomingRowViewType.NAMEDAY_CARD

    override val id: Long
        get() = namesLabel.hashCode().toLong()
}
