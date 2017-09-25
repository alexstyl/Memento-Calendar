package com.alexstyl.specialdates.upcoming

data class DateHeaderViewModel(val date: String) : UpcomingRowViewModel {

    override val viewType: Int
        get() = UpcomingRowViewType.DATE_HEADER

    override val id: Long
        get() = date.hashCode().toLong()

}
