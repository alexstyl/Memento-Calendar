package com.alexstyl.specialdates.events.bankholidays;

import com.alexstyl.specialdates.date.Date;

import java.util.ArrayList;
import java.util.List;

import static com.alexstyl.specialdates.date.DateConstants.AUGUST;
import static com.alexstyl.specialdates.date.DateConstants.MARCH;
import static com.alexstyl.specialdates.date.DateConstants.OCTOBER;

public class GreekBankHolidays {

    private final Date easter;
    private List<BankHoliday> bankHolidays;

    public GreekBankHolidays(Date easter) {
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
        bankHolidays.add(new BankHoliday("Πρωτοχρονιά", Date.on(1, 1, currentYear)));
        bankHolidays.add(new BankHoliday("Χριστούγεννα", Date.on(25, 12, currentYear)));
        bankHolidays.add(new BankHoliday("Θεοφάνεια", Date.on(6, 1, currentYear)));
        bankHolidays.add(new BankHoliday("Μεγάλη Παρασκευή", easter.minusDay(2)));
        bankHolidays.add(new BankHoliday("Πάσχα", easter));
        bankHolidays.add(new BankHoliday("2α Διακαινησίμου ", easter.addDay(1)));
        bankHolidays.add(new BankHoliday("25η Μαρτίου (επανάσταση του 1821)", Date.on(25, MARCH, currentYear)));
        bankHolidays.add(new BankHoliday("Κοίμηση της Θεοτόκου (Δεκαπενταύγουστος)", Date.on(15, AUGUST, currentYear)));
        bankHolidays.add(new BankHoliday("26η Οκτωβρίου (εορτή Αγ.Δημητρίου - πολιούχου Θεσσαλονίκης)", Date.on(26, OCTOBER, currentYear)));
        bankHolidays.add(new BankHoliday("28η Οκτωβρίου (επέτειος του ΟΧΙ)", Date.on(28, OCTOBER, currentYear)));
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
