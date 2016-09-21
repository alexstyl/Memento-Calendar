package com.alexstyl.specialdates.date;

import com.alexstyl.specialdates.Optional;

public class ParsedDate implements Date {

    private static final Optional<Integer> NO_YEAR = Optional.absent();
    private final int dayOfMonth;
    private final int month;
    private final Optional<Integer> year;

    public ParsedDate(int dayOfMonth, int month) {
        this.dayOfMonth = dayOfMonth;
        this.month = month;
        this.year = NO_YEAR;
    }

    public ParsedDate(int dayOfMonth, int month, int year) {
        this.dayOfMonth = dayOfMonth;
        this.month = month;
        this.year = new Optional<>(year);
    }

    @Override
    public int getDayOfMonth() {
        return dayOfMonth;
    }

    @Override
    public int getMonth() {
        return month;
    }

    public boolean hasYear() {
        return year.isPresent();
    }

    public int getYear() {
        return year.get();
    }
}
