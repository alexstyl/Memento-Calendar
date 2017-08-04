package com.alexstyl.specialdates.events.bankholidays;

import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.TimePeriod;

import java.util.ArrayList;
import java.util.List;

public final class BankHolidayProvider {

    private final GreekBankHolidaysCalculator bankHolidaysCalculator;

    public BankHolidayProvider(GreekBankHolidaysCalculator bankHolidaysCalculator) {
        this.bankHolidaysCalculator = bankHolidaysCalculator;
    }

    public List<BankHoliday> calculateBankHolidaysBetween(TimePeriod timePeriod) {
        if (isWithinTheSameYear(timePeriod)) {
            return calculateBankHolidaysFor(timePeriod);
        } else {
            ArrayList<BankHoliday> allBankholidays = new ArrayList<>();

            TimePeriod firstHalf = getfirstHalfOf(timePeriod);
            List<BankHoliday> firstHalfBankHolidays = calculateBankHolidaysFor(firstHalf);
            allBankholidays.addAll(firstHalfBankHolidays);

            TimePeriod secondHalf = getSecondHalfOf(timePeriod);
            List<BankHoliday> secondHalfBankHolidays = calculateBankHolidaysFor(secondHalf);
            allBankholidays.addAll(secondHalfBankHolidays);

            return allBankholidays;
        }
    }

    private static TimePeriod getfirstHalfOf(TimePeriod timePeriod) {
        Date startingDate = timePeriod.getStartingDate();
        return TimePeriod.Companion.between(
                startingDate,
                Date.Companion.endOfYear(startingDate.getYear())
        );
    }

    private static TimePeriod getSecondHalfOf(TimePeriod timePeriod) {
        Date endingDate = timePeriod.getEndingDate();
        return TimePeriod.Companion.between(
                Date.Companion.startOfTheYear(endingDate.getYear()),
                endingDate
        );
    }

    private List<BankHoliday> calculateBankHolidaysFor(TimePeriod timePeriod) {
        List<BankHoliday> bankHolidays = new ArrayList<>();
        int year = timePeriod.getStartingDate().getYear();
        List<BankHoliday> allBankHolidays = bankHolidaysCalculator.calculateBankholidaysForYear(year);
        for (BankHoliday bankHoliday : allBankHolidays) {
            if (timePeriod.containsDate(bankHoliday.getDate())) {
                bankHolidays.add(bankHoliday);
            }
        }
        return bankHolidays;
    }

    private boolean isWithinTheSameYear(TimePeriod timePeriod) {
        return timePeriod.getStartingDate().getYear() == timePeriod.getEndingDate().getYear();
    }

    public Optional<BankHoliday> calculateBankHolidayOn(Date date) {
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
