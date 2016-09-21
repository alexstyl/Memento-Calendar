package com.alexstyl.specialdates.date;

/**
 * A date on every year
 */
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AnnualEvent that = (AnnualEvent) o;

        if (dayOfMonth != that.dayOfMonth) {
            return false;
        }
        return month == that.month;

    }

    @Override
    public int hashCode() {
        int result = dayOfMonth;
        result = 31 * result + month;
        return result;
    }
}
