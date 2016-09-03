package com.alexstyl.specialdates.events.bankholidays;

import com.alexstyl.specialdates.date.DayDate;

import java.util.ArrayList;
import java.util.List;

public class GreekBankHolidays {

    private final DayDate easter;
    private List<BankHoliday> bankHolidays;

    public GreekBankHolidays(DayDate easter) {
        this.easter = easter;
    }

    public List<BankHoliday> getBankHolidaysForYear() {
        if (bankHolidays == null) {
            initialiseBankholidays();
        }
        return bankHolidays;
    }

    private void initialiseBankholidays() {
        int currentYear = easter.getYear();
        bankHolidays = new ArrayList<>();
        bankHolidays.add(new BankHoliday("Πρωτοχρονιά", DayDate.newInstance(1, 1, currentYear)));
        bankHolidays.add(new BankHoliday("Χριστούγεννα", DayDate.newInstance(25, 12, currentYear)));
        bankHolidays.add(new BankHoliday("Θεοφάνεια", DayDate.newInstance(6, 1, currentYear)));
        bankHolidays.add(new BankHoliday("Μεγάλη Παρασκευή", easter.minusDay(2)));
        bankHolidays.add(new BankHoliday("Πάσχα", easter));
        bankHolidays.add(new BankHoliday("2α Διακαινησίμου ", easter.addDay(1)));
        bankHolidays.add(new BankHoliday("25η Μαρτίου (επανάσταση του 1821)", DayDate.newInstance(25, DayDate.MARCH, currentYear)));
        bankHolidays.add(new BankHoliday("Κοίμηση της Θεοτόκου (Δεκαπενταύγουστος)", DayDate.newInstance(15, DayDate.AUGUST, currentYear)));
        bankHolidays.add(new BankHoliday("26η Οκτωβρίου (εορτή Αγ.Δημητρίου - πολιούχου Θεσσαλονίκης)", DayDate.newInstance(26, DayDate.OCTOBER, currentYear)));
        bankHolidays.add(new BankHoliday("28η Οκτωβρίου (επέτειος του ΟΧΙ)", DayDate.newInstance(28, DayDate.OCTOBER, currentYear)));
        bankHolidays.add(new BankHoliday("Τσικνοπέμπτη", easter.minusDay(59)));
        bankHolidays.add(new BankHoliday("Καθαρά Δευτέρα", easter.minusDay(48)));
        bankHolidays.add(new BankHoliday("Κυριακή των Βαίων", easter.minusDay(7)));
        bankHolidays.add(new BankHoliday("Σάββατο του Λαζάρου", easter.minusDay(8)));

        bankHolidays.add(new BankHoliday("Μεγάλη Δευτέρα", easter.minusDay(6)));
        bankHolidays.add(new BankHoliday("Του Θωμά", easter.addDay(7)));
        bankHolidays.add(new BankHoliday("Πεντηκοστή", easter.addDay(59)));
        bankHolidays.add(new BankHoliday("Αγ. Πνεύματος", easter.addDay(50)));
        bankHolidays.add(new BankHoliday("Αγίων Πάντων", easter.addDay(56)));
    }
}
