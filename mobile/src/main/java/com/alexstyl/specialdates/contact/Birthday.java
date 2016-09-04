package com.alexstyl.specialdates.contact;

import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.date.AnnualEvent;
import com.alexstyl.specialdates.date.DateDisplayStringCreator;
import com.alexstyl.specialdates.date.DayDate;

public class Birthday implements ShortDate {

    private final AnnualEvent date;
    private final Optional<Integer> yearOfBirth;

    public static Birthday on(DayDate date) {
        return new Birthday(date.getDayOfMonth(), date.getMonth(), date.getYear());
    }

    public Birthday(int day, int month) {
        this(day, month, DayDate.NO_YEAR);
    }

    public Birthday(int dayOfMonth, int month, int year) {
        this.date = new AnnualEvent(dayOfMonth, month);
        this.yearOfBirth = new Optional<>(year);
    }

    public int getDayOfMonth() {
        return date.getDayOfMonth();
    }

    public int getMonth() {
        return date.getMonth();
    }

    public int getYear() {
        return yearOfBirth.get();
    }

    public boolean includesYear() {
        return yearOfBirth.isPresent();
    }

    @Override
    public String toString() {
        return DateDisplayStringCreator.getInstance().fullyFormattedBirthday(this);
    }

    @Override
    public String toShortDate() {
        return DateDisplayStringCreator.getInstance().stringOf(date);
    }

    public int getAgeOnYear(int year) {
        return year - yearOfBirth.get();
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

        if (!date.equals(birthday.date)) {
            return false;
        }
        return yearOfBirth.equals(birthday.yearOfBirth);

    }

    @Override
    public int hashCode() {
        int result = date.hashCode();
        result = 31 * result + yearOfBirth.hashCode();
        return result;
    }
}
