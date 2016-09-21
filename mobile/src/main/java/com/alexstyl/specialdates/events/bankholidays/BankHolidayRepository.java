package com.alexstyl.specialdates.events.bankholidays;

import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.date.DayDate;
import com.alexstyl.specialdates.events.namedays.calendar.EasterCalculator;

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

    public Optional<BankHoliday> calculateBankholidayFor(DayDate date) {
        List<BankHoliday> bankHolidaysList;
        int year = date.getYear();
        if (isForNewYear(year)) {
            calculateHolidaysForYear(year);
        }
        bankHolidaysList = bankHolidays.getBankHolidaysForYear();

        for (BankHoliday bankHoliday : bankHolidaysList) {
            if (bankHoliday.getDate().equals(date)) {
                return new Optional<>(bankHoliday);
            }
        }
        return Optional.absent();
    }

    private boolean isForNewYear(int year) {
        return easter == null || easter.getYear() != year;
    }

    private void calculateHolidaysForYear(int year) {
        easter = calculator.calculateEasterForYear(year);
        bankHolidays = new GreekBankHolidays(easter);
    }

}
