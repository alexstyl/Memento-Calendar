package com.alexstyl.specialdates.date

data class TimePeriod constructor(val startingDate: Date, val endingDate: Date) {

    fun containsDate(date: Date): Boolean {
        return DateComparator.INSTANCE.compare(startingDate, date) <= 0 && DateComparator.INSTANCE.compare(date, endingDate) <= 0
    }

    companion object {

        fun between(startDate: Date, endDate: Date): TimePeriod {
            if (DateComparator.INSTANCE.compare(startDate, endDate) > 0) {
                throw IllegalArgumentException("starting Date was after end Date")
            }
            return TimePeriod(startDate, endDate)
        }

        fun aYearFromNow(): TimePeriod {
            val today = Date.today()
            val endDate = today.addDay(364)
            return TimePeriod.between(today, endDate)
        }

        fun aYearFrom(date: Date): TimePeriod {
            return TimePeriod.between(date, date.addDay(364))
        }
    }
}
