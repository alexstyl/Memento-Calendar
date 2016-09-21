package com.alexstyl.specialdates.date;

import com.alexstyl.specialdates.contact.ShortDate;

import org.joda.time.LocalDate;

/**
 * A specific date on a specific year
 */
public class DayDate implements Comparable<DayDate>, ShortDate, Date {

    private final LocalDate localDate;

    public static DayDate today() {
        LocalDate localDate = LocalDate.now();
        return new DayDate(localDate);
    }

    public static int todaysYear() {
        LocalDate localDate = LocalDate.now();
        return localDate.getYear();
    }

    public static DayDate newInstance(int dayOfMonth, int month, int year) {
        LocalDate localDate = new LocalDate(year, month, dayOfMonth);
        return new DayDate(localDate);
    }

    private DayDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public DayDate addDay(int i) {
        LocalDate addedDate = localDate.plusDays(i);
        return new DayDate(addedDate);
    }

    @Override
    public int getDayOfMonth() {
        return localDate.getDayOfMonth();
    }

    @Override
    public int getMonth() {
        return localDate.getMonthOfYear();
    }

    public int getYear() {
        return localDate.getYear();
    }

    public DayDate addMonth(int m) {
        LocalDate minusDate = localDate.plusMonths(m);
        return new DayDate(minusDate);
    }

    public DayDate minusMonth(int m) {
        LocalDate minusDate = localDate.minusMonths(m);
        return new DayDate(minusDate);
    }

    @Override
    public String toString() {
        return localDate.toString();
    }

    public long toMillis() {
        return localDate.toDate().getTime();
    }

    public boolean isBefore(DayDate date) {
        return compareTo(date) < 0;
    }

    public boolean isAfter(DayDate date) {
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
        return new DayDate(localDate);
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
        Integer daysOfYearsDifference = (yearOf(this) - yearOf(otherEvent)) * 365;
        return (otherDayOfYear - dayOfYear) - daysOfYearsDifference;
    }

    private Integer yearOf(DayDate otherEvent) {
        return otherEvent.getYear();
    }

    @Override
    public String toShortDate() {
        return DateDisplayStringCreator.getInstance().stringOf(this);
    }

    @Override
    public int compareTo(DayDate o) {
        return localDate.compareTo(o.localDate);
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

        return localDate.equals(dayDate.localDate);

    }

    @Override
    public int hashCode() {
        return localDate.hashCode();
    }
}
