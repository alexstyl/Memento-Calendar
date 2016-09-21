package com.alexstyl.specialdates.contact;

import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.date.AnnualEvent;
import com.alexstyl.specialdates.date.DateDisplayStringCreator;

public class Birthday implements ShortDate {

    private final AnnualEvent date;
    private final Optional<Integer> yearOfBirth;

    public static Birthday on(int dayOfMonth, int month) {
        return new Birthday(dayOfMonth, month, Optional.<Integer>absent());
    }

    public static Birthday on(int dayOfMonth, int month, int year) {
        if (year < 0) {
            throw new IllegalArgumentException("A birthday cannot have negative year. Use a different constructor if you do not need to specify the year");
        }
        return new Birthday(dayOfMonth, month, new Optional<>(year));
    }

    private Birthday(int dayOfMonth, int month, Optional<Integer> year) {
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
        return yearOfBirth.get();
    }

    public boolean hasYearOfBirth() {
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
