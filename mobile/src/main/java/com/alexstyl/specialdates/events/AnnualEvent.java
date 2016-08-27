package com.alexstyl.specialdates.events;

import com.alexstyl.specialdates.date.Date;

public class AnnualEvent implements Date {

    private final int dayOfMonth;
    private final int month;

    public AnnualEvent(int dayOfMonth, int month) {
        this.dayOfMonth = dayOfMonth;
        this.month = month;
    }

    @Override
    public String toString() {
        return DateDisplayStringCreator.getInstance().stringOf(this);
    }

    @Override
    public int getDayOfMonth() {
        return dayOfMonth;
    }

    @Override
    public int getMonth() {
        return month;
    }

    @Override
    public int getYear() {
        return Date.NO_YEAR;
    }

    @Override
    public boolean isBefore(Date date) {
        if (month > date.getMonth()) {
            return false;
        }
        if (month < date.getMonth()) {
            return true;
        }
        return dayOfMonth < date.getDayOfMonth();
    }

    @Override
    public boolean isAfter(Date date) {
        if (month < date.getMonth()) {
            return false;
        }
        if (month > date.getMonth()) {
            return true;
        }
        return dayOfMonth > date.getDayOfMonth();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AnnualEvent that = (AnnualEvent) o;

        return dayOfMonth == that.dayOfMonth
                && month == that.month;
    }

    @Override
    public int hashCode() {
        int result = dayOfMonth;
        result = 31 * result + month;
        return result;
    }
}
