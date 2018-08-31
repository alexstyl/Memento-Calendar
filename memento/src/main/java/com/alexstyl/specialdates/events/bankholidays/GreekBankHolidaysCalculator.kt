package com.alexstyl.specialdates.events.bankholidays

import com.alexstyl.specialdates.date.Months.AUGUST
import com.alexstyl.specialdates.date.Months.DECEMBER
import com.alexstyl.specialdates.date.Months.JANUARY
import com.alexstyl.specialdates.date.Months.MARCH
import com.alexstyl.specialdates.date.Months.OCTOBER
import com.alexstyl.specialdates.date.dateOn
import com.alexstyl.specialdates.events.namedays.calendar.OrthodoxEasterCalculator

class GreekBankHolidaysCalculator(private val easterCalculator: OrthodoxEasterCalculator) {

    fun calculateBankholidaysForYear(year: Int): List<BankHoliday> {
        val easter = easterCalculator.calculateEasterForYear(year)

        val bankHolidays = ArrayList<BankHoliday>()

        bankHolidays.add(BankHoliday("Πρωτοχρονιά", dateOn(1, JANUARY, year)))
        bankHolidays.add(BankHoliday("Χριστούγεννα", dateOn(25, DECEMBER, year)))
        bankHolidays.add(BankHoliday("Θεοφάνεια", dateOn(6, JANUARY, year)))
        bankHolidays.add(BankHoliday("Μεγάλη Παρασκευή", easter.minusDay(2)))
        bankHolidays.add(BankHoliday("Πάσχα", easter))
        bankHolidays.add(BankHoliday("2α Διακαινησίμου ", easter.addDay(1)))
        bankHolidays.add(BankHoliday("25η Μαρτίου (επανάσταση του 1821)", dateOn(25, MARCH, year)))
        bankHolidays.add(BankHoliday("Κοίμηση της Θεοτόκου (Δεκαπενταύγουστος)", dateOn(15, AUGUST, year)))
        bankHolidays.add(BankHoliday("26η Οκτωβρίου (εορτή Αγ.Δημητρίου - πολιούχου Θεσσαλονίκης)", dateOn(26, OCTOBER, year)))
        bankHolidays.add(BankHoliday("28η Οκτωβρίου (επέτειος του ΟΧΙ)", dateOn(28, OCTOBER, year)))
        bankHolidays.add(BankHoliday("Τσικνοπέμπτη", easter.minusDay(59)))
        bankHolidays.add(BankHoliday("Καθαρά Δευτέρα", easter.minusDay(48)))
        bankHolidays.add(BankHoliday("Κυριακή των Βαίων", easter.minusDay(7)))
        bankHolidays.add(BankHoliday("Σάββατο του Λαζάρου", easter.minusDay(8)))
        bankHolidays.add(BankHoliday("Μεγάλη Δευτέρα", easter.minusDay(6)))
        bankHolidays.add(BankHoliday("Του Θωμά", easter.addDay(7)))
        bankHolidays.add(BankHoliday("Πεντηκοστή", easter.addDay(59)))
        bankHolidays.add(BankHoliday("Αγ. Πνεύματος", easter.addDay(50)))
        bankHolidays.add(BankHoliday("Αγίων Πάντων", easter.addDay(56)))

        return bankHolidays.toList()
    }
}
