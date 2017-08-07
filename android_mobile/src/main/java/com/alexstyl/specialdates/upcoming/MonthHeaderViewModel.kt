package com.alexstyl.specialdates.upcoming

data class MonthHeaderViewModel constructor(val monthLabel: String) : UpcomingRowViewModel {

    override val viewType: Int
        get() = UpcomingRowViewType.MONTH

    override val id: Long
        get() = monthLabel.hashCode().toLong()

}
