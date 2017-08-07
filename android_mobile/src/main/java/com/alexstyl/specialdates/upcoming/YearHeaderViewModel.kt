package com.alexstyl.specialdates.upcoming

data class YearHeaderViewModel internal constructor(val year: String) : UpcomingRowViewModel {

    override val viewType: Int
        get() = UpcomingRowViewType.YEAR

    override val id: Long
        get() = hashCode().toLong()
}
