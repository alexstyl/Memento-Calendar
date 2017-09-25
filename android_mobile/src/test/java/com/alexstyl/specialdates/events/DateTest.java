package com.alexstyl.specialdates.events;

import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.DateComparator;
import com.alexstyl.specialdates.date.MonthInt;

import org.junit.Test;

import static com.alexstyl.specialdates.date.Months.*;
import static org.fest.assertions.api.Assertions.assertThat;

public class DateTest {

    private static final DateComparator comparator = DateComparator.INSTANCE;

    private static final int DAY = 5;
    private static final
    @MonthInt
    int MONTH = 10;

    private static final Date ANY_DATE = Date.Companion.on(DAY, MONTH, 1990);

    @Test
    public void whenAddingADayOfMonth_thenIncreaseNormally() {

        Date after = ANY_DATE.addDay(1);
        int expectedDay = DAY + 1;

        assertThat(after.getDayOfMonth()).isEqualTo(expectedDay);
    }

    @Test
    public void givenAEndOfTheYearDate_whenAddingOneDay_thenTheFirstDayOftheNextYearIsReturned() {
        Date lastDayOfYear = Date.Companion.on(31, DECEMBER, 1990);
        Date firstDayOfNextYear = lastDayOfYear.addDay(1);

        assertThat(firstDayOfNextYear.getMonth()).isEqualTo(1);
        assertThat(firstDayOfNextYear.getDayOfMonth()).isEqualTo(1);
        int nextYear = lastDayOfYear.getYear() + 1;
        assertThat(firstDayOfNextYear.getYear()).isEqualTo(nextYear);
    }

    @Test
    public void testOneDayAhead() {
        Date firstDayOfYear = Date.Companion.on(1, JANUARY, 1990);
        Date secondDayOfYear = Date.Companion.on(2, JANUARY, 1990);
        assertThat(firstDayOfYear.daysDifferenceTo(secondDayOfYear)).isEqualTo(1);
    }

    @Test
    public void testOneYearAhead() {
        Date firstDayOfYear = Date.Companion.on(1, JANUARY, 1990);
        Date secondDayOfYear = Date.Companion.on(1, JANUARY, 1991);
        assertThat(firstDayOfYear.daysDifferenceTo(secondDayOfYear)).isEqualTo(365);
    }

    @Test
    public void testTwoYearAhead() {
        Date firstDayOfYear = Date.Companion.on(1, JANUARY, 1990);
        Date secondDayOfYear = Date.Companion.on(1, JANUARY, 1992);
        assertThat(firstDayOfYear.daysDifferenceTo(secondDayOfYear)).isEqualTo(365 * 2);
    }

    @Test
    public void compareFutureDayDate() {
        int result = comparator.compare(Date.Companion.on(1, JANUARY, 1990), Date.Companion.on(2, JANUARY, 1990));
        assertThat(result).isNegative();
    }

    @Test
    public void compareSameDayDate() {
        int result = comparator.compare(Date.Companion.on(1, JANUARY, 1990), Date.Companion.on(1, JANUARY, 1990));
        assertThat(result).isZero();
    }

    @Test
    public void comparePastDayDate() {
        int result = comparator.compare(Date.Companion.on(1, JANUARY, 1990), Date.Companion.on(1, JANUARY, 1980));
        assertThat(result).isPositive();
    }

    @Test
    public void compareFutureDate() {
        int result = comparator.compare(Date.Companion.on(1, JANUARY, 1990), Date.Companion.on(2, JANUARY));
        assertThat(result).isNegative();
    }

    @Test
    public void compareSameDate() {
        int result = comparator.compare(Date.Companion.on(1, JANUARY, 1990), Date.Companion.on(1, JANUARY));
        assertThat(result).isZero();
    }

    @Test
    public void comparePastDate() {
        int result = comparator.compare(Date.Companion.on(2, JANUARY, 1990), Date.Companion.on(1, JANUARY));
        assertThat(result).isPositive();
    }

    @Test
    public void whenComparingToSameDate_thenTheyAreEqual() {
        Date firstDate = Date.Companion.on(16, APRIL, 1991);
        Date secondDate = Date.Companion.on(16, APRIL, 1991);

        assertThat(firstDate.equals(secondDate)).isTrue();
    }

    @Test
    public void whenComparingToDateWithDifferentYear_thenTheyAreNotEqual() {
        Date firstDate = Date.Companion.on(16, APRIL, 1991);
        Date secondDate = Date.Companion.on(16, APRIL, 1987);

        assertThat(firstDate.equals(secondDate)).isFalse();
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsException_whenInvalidDateIsCreated() {
        Date.Companion.on(31, FEBRUARY, 1991);
    }
}
