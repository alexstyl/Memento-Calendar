package com.alexstyl.specialdates.events.bankholidays

import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.DateComparator

data class BankHoliday(val holidayName: String, val date: Date) : Comparable<BankHoliday> {

    override fun toString(): String {
        return holidayName
    }

    override fun compareTo(another: BankHoliday): Int {
        return DateComparator.INSTANCE.compare(date, another.date)
    }
}
