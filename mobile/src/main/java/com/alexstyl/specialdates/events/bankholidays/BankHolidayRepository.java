package com.alexstyl.specialdates.events.bankholidays;

import android.support.annotation.Nullable;

import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.DayDate;
import com.alexstyl.specialdates.events.namedays.EasterCalculator;

import java.util.List;

public class BankHolidayRepository {

    private final EasterCalculator calculator;

    private DayDate easter;
    private GreekBankHolidays bankHolidays;

    public BankHolidayRepository(EasterCalculator calculator) {
        this.calculator = calculator;
    }

    public void preloadHolidaysForYear(int year) {
        calculateHolidaysForYear(year);
    }

    @Nullable
    public BankHoliday calculateBankholidayFor(Date date) {
        List<BankHoliday> bankHolidaysList;
        int year = date.getYear();
        if (isForNewYear(year)) {
            calculateHolidaysForYear(year);
        }
        bankHolidaysList = bankHolidays.getBankHolidaysForYear();

        for (BankHoliday bankHoliday : bankHolidaysList) {
            if (bankHoliday.getDate().equals(date)) {
                return bankHoliday;
            }
        }

        return null;
    }

    private boolean isForNewYear(int year) {
        return easter == null || easter.getYear() != year;
    }

    private void calculateHolidaysForYear(int year) {
        easter = calculator.calculateEasterForYear(year);
        bankHolidays = new GreekBankHolidays(easter);
    }

}
