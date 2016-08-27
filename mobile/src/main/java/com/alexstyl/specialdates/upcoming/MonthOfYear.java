package com.alexstyl.specialdates.upcoming;

import com.alexstyl.specialdates.date.CelebrationDate;

public class MonthOfYear {

    private final int month;
    private final int year;

    public static MonthOfYear of(CelebrationDate date) {
        return new MonthOfYear(date.getMonth(), date.getYear());
    }

    public MonthOfYear(int month, int year) {
        this.month = month;
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }
}
