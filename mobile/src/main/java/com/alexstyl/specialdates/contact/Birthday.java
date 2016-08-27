package com.alexstyl.specialdates.contact;

import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.AnnualEvent;
import com.alexstyl.specialdates.events.DateDisplayStringCreator;
import com.alexstyl.specialdates.events.DayDate;

public class Birthday {

    private final AnnualEvent date;
    private final int yearOfBirth;

    public static Birthday on(DayDate date) {
        return new Birthday(date.getDayOfMonth(), date.getMonth(), date.getYear());
    }

    public Birthday(int day, int month) {
        this(day, month, DayDate.NO_YEAR);
    }

    public Birthday(int dayOfMonth, int month, int year) {
        this.date = new AnnualEvent(dayOfMonth, month);
        this.yearOfBirth = year;
    }

    public int getDayOfMonth() {
        return date.getDayOfMonth();
    }

    public int getMonth() {
        return date.getMonth();
    }

    public int getYear() {
        return yearOfBirth;
    }

    public boolean includesYear() {
        return yearOfBirth != Date.NO_YEAR;
    }

    @Override
    public String toString() {
        return DateDisplayStringCreator.getInstance().stringOf(date);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Birthday birthday = (Birthday) o;

        return yearOfBirth == birthday.yearOfBirth
                && date != null ? date.equals(birthday.date) : birthday.date == null;
    }

    @Override
    public int hashCode() {
        int result = date != null ? date.hashCode() : 0;
        result = 31 * result + yearOfBirth;
        return result;
    }

    public int getAgeOnYear(int year) {
        return year - yearOfBirth;
    }
}
