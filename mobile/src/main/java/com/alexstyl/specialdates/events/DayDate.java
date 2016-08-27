package com.alexstyl.specialdates.events;

import android.support.annotation.NonNull;

import com.alexstyl.specialdates.date.Date;

import java.util.Calendar;

import org.joda.time.LocalDate;

public class DayDate implements Comparable<Date>, Date {

    public static final int NO_YEAR = -1;

    public static final int JANUARY = 1;
    public static final int FEBRUARY = 2;
    public static final int MARCH = 3;
    public static final int APRIL = 4;
    public static final int MAY = 5;
    public static final int JUNE = 6;
    public static final int JULY = 7;
    public static final int AUGUST = 8;
    public static final int SEPTEMBER = 9;
    public static final int OCTOBER = 10;
    public static final int NOVEMBER = 11;
    public static final int DECEMBER = 12;

    public static int MONDAY = 1;
    public static int TUESDAY = 2;
    public static int WEDNESDAY = 3;
    public static int THURSDAY = 4;
    public static int FRIDAY = 5;
    public static int SATURDAY = 6;
    public static int SUNDAY = 7;

    private final LocalDate localDate;
    private final boolean includesYear;

    public static DayDate today() {
        LocalDate localDate = LocalDate.now();
        return new DayDate(localDate, true);
    }

    public static DayDate newInstance(DayDate dayDate) {
        int dayOfMonth = dayDate.getDayOfMonth();
        int month = dayDate.getMonth();
        int year = dayDate.getYear();

        return DayDate.newInstance(dayOfMonth, month, year);
    }

    public static DayDate newInstance(int dayOfMonth, int month) {
        return newInstance(dayOfMonth, month, NO_YEAR);
    }

    public static DayDate newInstance(int dayOfMonth, int month, int year) {
        LocalDate localDate = new LocalDate(year, month, dayOfMonth);
        boolean includesYear = year != NO_YEAR;
        return new DayDate(localDate, includesYear);
    }

    private DayDate(LocalDate localDate, boolean includesYear) {
        this.localDate = localDate;
        this.includesYear = includesYear;
    }

    public static DayDate newInstance(Calendar easter) {
        int dayOfMonth = easter.get(Calendar.DAY_OF_MONTH);
        int month = easter.get(Calendar.MONTH);
        int year = easter.get(Calendar.YEAR);

        return DayDate.newInstance(dayOfMonth, month, year);
    }

    public DayDate addDay(int i) {
        LocalDate addedDate = localDate.plusDays(i);
        return new DayDate(addedDate, containsYear(addedDate));
    }

    @Override
    public int getDayOfMonth() {
        return localDate.getDayOfMonth();
    }

    @Override
    public int getMonth() {
        return localDate.getMonthOfYear();
    }

    @Override
    public int getYear() {
        if (includesYear) {
            return localDate.getYear();
        }
        return NO_YEAR;
    }

    public DayDate addMonth(int m) {
        LocalDate minusDate = localDate.plusMonths(m);
        return new DayDate(minusDate, containsYear(minusDate));
    }

    public DayDate minusMonth(int m) {
        LocalDate minusDate = localDate.minusMonths(m);
        return new DayDate(minusDate, containsYear(minusDate));
    }

    @Override
    public String toString() {
        return DateDisplayStringCreator.getInstance().stringOf(this);
    }

    private static boolean containsYear(LocalDate addedDate) {
        return addedDate.getYear() != NO_YEAR;
    }

    @Override
    public int compareTo(@NonNull Date another) {
        if (this.getYear() != another.getYear()) {
            return this.getYear() - another.getYear();
        }
        if (this.getMonth() != another.getMonth()) {
            return this.getMonth() - another.getMonth();
        }
        return getDayOfMonth() - another.getDayOfMonth();
    }

    public long toMillis() {
        return localDate.toDate().getTime();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DayDate dayDate = (DayDate) o;

        if (includesYear != dayDate.includesYear) {
            return false;
        }
        return localDate.equals(dayDate.localDate);

    }

    @Override
    public int hashCode() {
        int result = localDate.hashCode();
        result = 31 * result + (includesYear ? 1 : 0);
        return result;
    }

    @Override
    public boolean isBefore(Date date) {
        return compareTo(date) < 0;
    }

    @Override
    public boolean isAfter(Date date) {
        return compareTo(date) > 0;
    }

    public AnnualEvent asAnnualEvent() {
        return new AnnualEvent(getDayOfMonth(), getMonth());
    }

    public static DayDate startOfTheYear(int currentYear) {
        return DayDate.newInstance(1, 1, currentYear);
    }

    public DayDate minusDay(int days) {
        LocalDate localDate = this.localDate.minusDays(days);
        return new DayDate(localDate, true);
    }

    public static DayDate endOfYear(int currentYear) {
        return DayDate.newInstance(31, 12, currentYear);
    }

    public int getDayOfWeek() {
        return localDate.getDayOfWeek();
    }

    public DayDate addWeek(int weeks) {
        return addDay(7 * weeks);
    }

    public int daysDifferenceTo(DayDate otherEvent) {
        int dayOfYear = localDate.dayOfYear().get();
        int otherDayOfYear = otherEvent.localDate.dayOfYear().get();
        int daysOfYearsDifference = (getYear() - otherEvent.getYear()) * 365;
        return (otherDayOfYear - dayOfYear) - daysOfYearsDifference;
    }
}
