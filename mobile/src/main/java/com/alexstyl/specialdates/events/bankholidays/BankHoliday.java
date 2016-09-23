package com.alexstyl.specialdates.events.bankholidays;

import android.support.annotation.NonNull;

import com.alexstyl.specialdates.date.DateComparator;
import com.alexstyl.specialdates.date.DayDate;

public class BankHoliday implements Comparable<BankHoliday> {

    private final String holidayName;
    private final DayDate date;

    public BankHoliday(String holidayName, DayDate date) {
        this.holidayName = holidayName;
        this.date = date;
    }

    public String getHolidayName() {
        return holidayName;
    }

    public DayDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        return holidayName;
    }

    @Override
    public int compareTo(@NonNull BankHoliday another) {
        return DateComparator.get().compare(date, another.date);
    }
}
