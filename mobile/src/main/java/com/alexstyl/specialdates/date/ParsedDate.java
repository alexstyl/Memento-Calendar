package com.alexstyl.specialdates.date;

import com.alexstyl.specialdates.Optional;

import java.util.Locale;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ParsedDate that = (ParsedDate) o;

        if (dayOfMonth != that.dayOfMonth) {
            return false;
        }
        if (month != that.month) {
            return false;
        }
        return year.equals(that.year);

    }

    @Override
    public int hashCode() {
        int result = dayOfMonth;
        result = 31 * result + month;
        result = 31 * result + year.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format(Locale.US, "%d-%d-%d", dayOfMonth, month, year.isPresent() ? year.get() : "-");
    }
}
