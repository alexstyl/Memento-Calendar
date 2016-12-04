package com.alexstyl.specialdates.upcoming;

import com.alexstyl.specialdates.date.CelebrationDate;
import com.alexstyl.specialdates.date.MonthInt;

public class MonthOfYear {
    @MonthInt
    private final int month;
    private final int year;

    public static MonthOfYear of(CelebrationDate date) {
        return new MonthOfYear(date.getMonth(), date.getYear());
    }

    private MonthOfYear(int month, int year) {
        this.month = month;
        this.year = year;
    }

    @MonthInt
    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }
}
