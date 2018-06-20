package com.alexstyl.specialdates.events.bankholidays

import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.TimePeriod
import java.util.ArrayList

class BankHolidayProvider(private val bankHolidaysCalculator: GreekBankHolidaysCalculator) {

    fun calculateBankHolidaysBetween(timePeriod: TimePeriod): List<BankHoliday> =
            if (isWithinTheSameYear(timePeriod)) {
                calculateBankHolidaysFor(timePeriod)
            } else {
                val allBankholidays = ArrayList<BankHoliday>()

                val firstHalf = getfirstHalfOf(timePeriod)
                val firstHalfBankHolidays = calculateBankHolidaysFor(firstHalf)
                allBankholidays.addAll(firstHalfBankHolidays)

                val secondHalf = getSecondHalfOf(timePeriod)
                val secondHalfBankHolidays = calculateBankHolidaysFor(secondHalf)
                allBankholidays.addAll(secondHalfBankHolidays)

                allBankholidays
            }

    private fun getfirstHalfOf(timePeriod: TimePeriod): TimePeriod {
        val startingDate = timePeriod.startingDate
        return TimePeriod.between(
                startingDate,
                Date.endOfYear(startingDate.year)
        )
    }

    private fun getSecondHalfOf(timePeriod: TimePeriod): TimePeriod {
        val endingDate = timePeriod.endingDate
        return TimePeriod.between(
                Date.startOfYear(endingDate.year),
                endingDate
        )
    }

    private fun calculateBankHolidaysFor(timePeriod: TimePeriod): List<BankHoliday> {
        val bankHolidays = ArrayList<BankHoliday>()
        val year = timePeriod.startingDate.year
        val allBankHolidays = bankHolidaysCalculator.calculateBankholidaysForYear(year)
        for (bankHoliday in allBankHolidays) {
            if (timePeriod.containsDate(bankHoliday.date)) {
                bankHolidays.add(bankHoliday)
            }
        }
        return bankHolidays
    }

    private fun isWithinTheSameYear(timePeriod: TimePeriod): Boolean {
        return timePeriod.startingDate.year == timePeriod.endingDate.year
    }

    fun calculateBankHolidayOn(date: Date): BankHoliday? =
            bankHolidaysCalculator
                    .calculateBankholidaysForYear(date.year)
                    .find { it.date == date }

}
