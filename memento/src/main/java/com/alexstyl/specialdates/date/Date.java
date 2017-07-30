package com.alexstyl.specialdates.date;

import com.alexstyl.specialdates.Optional;

import java.util.Locale;

import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;
import org.joda.time.IllegalFieldValueException;
import org.joda.time.LocalDate;

/**
 * A specific date on a specific year
 */
public class Date implements Comparable<Date> {

    private final LocalDate localDate;
    private final Optional<Integer> year;

    public static int CURRENT_YEAR;

    static {
        CURRENT_YEAR = LocalDate.now().getYear();
    }

    public static Date today() {
        LocalDate localDate = LocalDate.now();
        return new Date(localDate, new Optional<>(localDate.getYear()));
    }

    public static Date on(int dayOfMonth, @MonthInt int month) {
        LocalDate localDate = new LocalDate(DateConstants.NO_YEAR, month, dayOfMonth);
        return new Date(localDate, Optional.<Integer>absent());
    }

    public static Date on(int dayOfMonth, @MonthInt int month, int year) {
        if (year <= 0) {
            throw new IllegalArgumentException(
                    "Do not call DayDate.on() if no year is present. Call the respective method without the year argument instead");
        }
        try {
            LocalDate localDate = new LocalDate(year, month, dayOfMonth);
            return new Date(localDate, new Optional<>(year));
        } catch (IllegalFieldValueException a) {
            throw new IllegalArgumentException(String.format(Locale.US, "%d/%d/%d is invalid", dayOfMonth, month, year));
        }
    }

    public static Date fromMillis(long timeInMilis) {
        LocalDate dateTime = new DateTime(timeInMilis).toLocalDate();
        return new Date(dateTime, new Optional<>(dateTime.getYear()));
    }

    private Date(LocalDate localDate, Optional<Integer> year) {
        this.localDate = localDate;
        this.year = year;
    }

    public Date addDay(int i) {
        LocalDate addedDate = localDate.plusDays(i);
        return new Date(addedDate, new Optional<>(addedDate.getYear()));
    }

    public int getDayOfMonth() {
        return localDate.getDayOfMonth();
    }

    @MonthInt
    @SuppressWarnings("WrongConstant") // JodaTime follows the same indexing as our project
    public int getMonth() {
        return localDate.getMonthOfYear();
    }

    public int getYear() {
        return year.get();
    }

    public long toMillis() {
        return localDate.toDate().getTime();
    }

    public static Date startOfTheYear(int currentYear) {
        return Date.on(1, DateConstants.JANUARY, currentYear);
    }

    public static Date endOfYear(int currentYear) {
        return Date.on(31, DateConstants.DECEMBER, currentYear);
    }

    public Date minusDay(int days) {
        LocalDate minusDays = localDate.minusDays(days);
        return new Date(minusDays, new Optional<>(minusDays.getYear()));
    }

    public int getDayOfWeek() {
        return localDate.getDayOfWeek();
    }

    public Date addWeek(int weeks) {
        return addDay(7 * weeks);
    }

    public int daysDifferenceTo(Date otherEvent) {
        int dayOfYear = localDate.dayOfYear().get();
        int otherDayOfYear = otherEvent.localDate.dayOfYear().get();
        int daysOfYearsDifference = (getYear() - otherEvent.getYear()) * 365;
        return (otherDayOfYear - dayOfYear) - daysOfYearsDifference;
    }

    public boolean hasYear() {
        return year.isPresent();
    }

    public boolean hasNoYear() {
        return !year.isPresent();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Date date = (Date) o;

        if (!localDate.equals(date.localDate)) {
            return false;
        }
        return year.equals(date.year);

    }

    @Override
    public int hashCode() {
        int result = localDate.hashCode();
        result = 31 * result + year.hashCode();
        return result;
    }

    @Override
    public int compareTo(@NotNull Date right) {
        if (this.hasYear() && right.hasYear()) {
            int yearOne = this.getYear();
            int yearTwo = right.getYear();
            if (yearOne > yearTwo) {
                return 1;
            } else if (yearOne < yearTwo) {
                return -1;
            }
        }
        if (this.getMonth() < right.getMonth()) {
            return -1;
        } else if (this.getMonth() > right.getMonth()) {
            return 1;
        }
        return this.getDayOfMonth() - right.getDayOfMonth();
    }

}
