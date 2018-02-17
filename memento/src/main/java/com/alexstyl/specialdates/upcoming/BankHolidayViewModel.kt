package com.alexstyl.specialdates.upcoming

data class BankHolidayViewModel(val bankHolidayName: String) : UpcomingRowViewModel {
    override val viewType: Int
        get() = UpcomingRowViewType.BANKHOLIDAY
    override val id: Long
        get() = bankHolidayName.hashCode().toLong()
}
