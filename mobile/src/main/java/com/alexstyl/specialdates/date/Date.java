package com.alexstyl.specialdates.date;

import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.contact.ShortDate;

import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.IllegalFieldValueException;
import org.joda.time.LocalDate;

import static com.alexstyl.specialdates.date.DateConstants.*;

/**
 * A specific date on a specific year
 */
public class Date implements ShortDate {

    public static final Date BEGINNING_OF_TIME = Date.on(1, JANUARY, NO_YEAR);
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
        LocalDate localDate = new LocalDate(NO_YEAR, month, dayOfMonth);
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
            if (month == DateConstants.FEBRUARY && dayOfMonth == 29) {
                // move the birthday one further
                LocalDate localDate = new LocalDate(year, DateConstants.MARCH, 1);
                return new Date(localDate, new Optional<>(year));
            } else {
                throw new IllegalArgumentException(String.format(Locale.US, "%d/%d/%d is invalid", dayOfMonth, month, year));
            }
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
        return Date.on(1, JANUARY, currentYear);
    }

    public static Date endOfYear(int currentYear) {
        return Date.on(31, DECEMBER, currentYear);
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

    @Override
    public String toShortDate() {
        return DateDisplayStringCreator.INSTANCE.stringOf(this);
    }

    public boolean hasYear() {
        return year.isPresent();
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
    public String toString() {
        return DateDisplayStringCreator.INSTANCE.stringOf(this);
    }

}
