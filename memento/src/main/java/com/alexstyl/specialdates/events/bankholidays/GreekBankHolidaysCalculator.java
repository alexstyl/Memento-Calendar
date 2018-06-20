package com.alexstyl.specialdates.events.bankholidays;

import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.namedays.calendar.OrthodoxEasterCalculator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.alexstyl.specialdates.date.Months.*;

public class GreekBankHolidaysCalculator {
    private final OrthodoxEasterCalculator easterCalculator;

    public GreekBankHolidaysCalculator(OrthodoxEasterCalculator easterCalculator) {
        this.easterCalculator = easterCalculator;
    }

    @SuppressWarnings("checkstyle:magicnumber")
    List<BankHoliday> calculateBankholidaysForYear(int year) {
        Date easter = easterCalculator.calculateEasterForYear(year);

        List<BankHoliday> bankHolidays = new ArrayList<>();
        bankHolidays.add(new BankHoliday("Πρωτοχρονιά", Date.Companion.on(1, JANUARY, year)));
        bankHolidays.add(new BankHoliday("Χριστούγεννα", Date.Companion.on(25, DECEMBER, year)));
        bankHolidays.add(new BankHoliday("Θεοφάνεια", Date.Companion.on(6, JANUARY, year)));
        bankHolidays.add(new BankHoliday("Μεγάλη Παρασκευή", easter.minusDay(2)));
        bankHolidays.add(new BankHoliday("Πάσχα", easter));
        bankHolidays.add(new BankHoliday("2α Διακαινησίμου ", easter.addDay(1)));
        bankHolidays.add(new BankHoliday("25η Μαρτίου (επανάσταση του 1821)", Date.Companion.on(25, MARCH, year)));
        bankHolidays.add(new BankHoliday("Κοίμηση της Θεοτόκου (Δεκαπενταύγουστος)", Date.Companion.on(15, AUGUST, year)));
        bankHolidays.add(new BankHoliday("26η Οκτωβρίου (εορτή Αγ.Δημητρίου - πολιούχου Θεσσαλονίκης)", Date.Companion.on(26, OCTOBER, year)));
        bankHolidays.add(new BankHoliday("28η Οκτωβρίου (επέτειος του ΟΧΙ)", Date.Companion.on(28, OCTOBER, year)));
        bankHolidays.add(new BankHoliday("Τσικνοπέμπτη", easter.minusDay(59)));
        bankHolidays.add(new BankHoliday("Καθαρά Δευτέρα", easter.minusDay(48)));
        bankHolidays.add(new BankHoliday("Κυριακή των Βαίων", easter.minusDay(7)));
        bankHolidays.add(new BankHoliday("Σάββατο του Λαζάρου", easter.minusDay(8)));
        bankHolidays.add(new BankHoliday("Μεγάλη Δευτέρα", easter.minusDay(6)));
        bankHolidays.add(new BankHoliday("Του Θωμά", easter.addDay(7)));
        bankHolidays.add(new BankHoliday("Πεντηκοστή", easter.addDay(59)));
        bankHolidays.add(new BankHoliday("Αγ. Πνεύματος", easter.addDay(50)));
        bankHolidays.add(new BankHoliday("Αγίων Πάντων", easter.addDay(56)));
        return Collections.unmodifiableList(bankHolidays);
    }
}
