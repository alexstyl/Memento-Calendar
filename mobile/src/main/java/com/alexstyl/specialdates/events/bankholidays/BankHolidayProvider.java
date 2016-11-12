package com.alexstyl.specialdates.events.bankholidays;

import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.upcoming.TimePeriod;

import java.util.ArrayList;
import java.util.List;

public final class BankHolidayProvider {

    private final GreekBankHolidaysCalculator bankHolidaysCalculator;

    public BankHolidayProvider(GreekBankHolidaysCalculator bankHolidaysCalculator) {
        this.bankHolidaysCalculator = bankHolidaysCalculator;
    }

    public List<BankHoliday> getBankHolidayFor(TimePeriod timePeriod) {
        List<BankHoliday> allBankHolidays = bankHolidaysCalculator.calculateBankholidaysForYear(timePeriod.getYear());
        List<BankHoliday> bankHolidays = new ArrayList<>();
        for (BankHoliday bankHoliday : allBankHolidays) {
            if (timePeriod.containsDate(bankHoliday.getDate())) {
                bankHolidays.add(bankHoliday);
            }
        }
        return bankHolidays;
    }

    public Optional<BankHoliday> getBankHolidayFor(Date date) {
        List<BankHoliday> bankHolidaysList;
        bankHolidaysList = bankHolidaysCalculator.calculateBankholidaysForYear(date.getYear());
        for (BankHoliday bankHoliday : bankHolidaysList) {
            if (bankHoliday.getDate().equals(date)) {
                return new Optional<>(bankHoliday);
            }
        }
        return Optional.absent();
    }

}
