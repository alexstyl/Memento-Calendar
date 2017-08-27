package com.alexstyl.specialdates.upcoming

data class DateHeaderViewModel(val monthLabel: String) : UpcomingRowViewModel {

    override val viewType: Int
        get() = UpcomingRowViewType.DATE_HEADER

    override val id: Long
        get() = monthLabel.hashCode().toLong()

}
