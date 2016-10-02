package com.alexstyl.specialdates.events.bankholidays;

import android.support.annotation.NonNull;

import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.DateComparator;

public class BankHoliday implements Comparable<BankHoliday> {

    private final String holidayName;
    private final Date date;

    public BankHoliday(String holidayName, Date date) {
        this.holidayName = holidayName;
        this.date = date;
    }

    public String getHolidayName() {
        return holidayName;
    }

    public Date getDate() {
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
