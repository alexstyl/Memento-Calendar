package com.alexstyl.specialdates.events;

import com.alexstyl.specialdates.date.AnnualEvent;
import com.alexstyl.specialdates.date.DateComparator;
import com.alexstyl.specialdates.date.DayDate;
import com.alexstyl.specialdates.date.MonthInt;

import org.junit.Test;

import static com.alexstyl.specialdates.date.Date.*;
import static org.fest.assertions.api.Assertions.assertThat;

public class DayDateTest {

    private static final int DAY = 5;
    private static final
    @MonthInt
    int MONTH = 10;
    private static final DayDate ANY_DATE = DayDate.newInstance(DAY, MONTH, 1990);

    @Test
    public void whenAddingADayOfMonth_thenIncreaseNormally() {

        DayDate after = ANY_DATE.addDay(1);
        int expectedDay = DAY + 1;

        assertThat(after.getDayOfMonth()).isEqualTo(expectedDay);
    }

    @Test(expected = Exception.class)
    public void tests() {
        DayDate dayDate = DayDate.newInstance(32, MARCH, 2016);
        dayDate.minusMonth(1);

    }

    @Test
    public void whenSubtractingOneMonth_thenMonthIsDecreasedByOne() {

        DayDate after = ANY_DATE.minusMonth(1);

        int expectedMonth = MONTH - 1;

        assertThat(after.getMonth()).isEqualTo(expectedMonth);

    }

    @Test
    public void givenAEndOfTheYearDate_whenAddingOneDay_thenTheFirstDayOftheNextYearIsReturned() {
        DayDate lastDayOfYear = DayDate.newInstance(31, DECEMBER, 1990);
        DayDate firstDayOfNextYear = lastDayOfYear.addDay(1);

        assertThat(firstDayOfNextYear.getMonth()).isEqualTo(1);
        assertThat(firstDayOfNextYear.getDayOfMonth()).isEqualTo(1);
        int nextYear = lastDayOfYear.getYear() + 1;
        assertThat(firstDayOfNextYear.getYear()).isEqualTo(nextYear);
    }

    @Test
    public void whenNewYearMinus() {
        DayDate firstDayOfYear = DayDate.newInstance(1, JANUARY, 1990);
        DayDate firstDayOfPreviousMonth = firstDayOfYear.minusMonth(1);

        assertThat(firstDayOfPreviousMonth.getMonth()).isEqualTo(12);
    }

    @Test
    public void testOneDayAhead() {
        DayDate firstDayOfYear = DayDate.newInstance(1, JANUARY, 1990);
        DayDate secondDayOfYear = DayDate.newInstance(2, JANUARY, 1990);
        assertThat(firstDayOfYear.daysDifferenceTo(secondDayOfYear)).isEqualTo(1);
    }

    @Test
    public void testOneYearAhead() {
        DayDate firstDayOfYear = DayDate.newInstance(1, JANUARY, 1990);
        DayDate secondDayOfYear = DayDate.newInstance(1, JANUARY, 1991);
        assertThat(firstDayOfYear.daysDifferenceTo(secondDayOfYear)).isEqualTo(365);
    }

    @Test
    public void testTwoYearAhead() {
        DayDate firstDayOfYear = DayDate.newInstance(1, JANUARY, 1990);
        DayDate secondDayOfYear = DayDate.newInstance(1, JANUARY, 1992);
        assertThat(firstDayOfYear.daysDifferenceTo(secondDayOfYear)).isEqualTo(365 * 2);
    }

    @Test
    public void toShortDateWithYear() {
        DayDate dayDate = DayDate.newInstance(1, JANUARY, 1990);
        assertThat(dayDate.toShortDate()).isEqualTo("1990-01-01");
    }

    private DateComparator comparator = DateComparator.get();

    @Test
    public void compareFutureDayDate() {
        int result = comparator.compare(DayDate.newInstance(1, JANUARY, 1990), DayDate.newInstance(2, JANUARY, 1990));
        assertThat(result).isNegative();
    }

    @Test
    public void compareSameDayDate() {
        int result = comparator.compare(DayDate.newInstance(1, JANUARY, 1990), DayDate.newInstance(1, JANUARY, 1990));
        assertThat(result).isZero();
    }

    @Test
    public void comparePastDayDate() {
        int result = comparator.compare(DayDate.newInstance(1, JANUARY, 1990), DayDate.newInstance(1, JANUARY, 1980));
        assertThat(result).isPositive();
    }

    @Test
    public void compareFutureDate() {
        int result = comparator.compare(DayDate.newInstance(1, JANUARY, 1990), new AnnualEvent(2, JANUARY));
        assertThat(result).isNegative();
    }

    @Test
    public void compareSameDate() {
        int result = comparator.compare(DayDate.newInstance(1, JANUARY, 1990), new AnnualEvent(1, JANUARY));
        assertThat(result).isZero();
    }

    @Test
    public void comparePastDate() {
        int result = comparator.compare(DayDate.newInstance(2, JANUARY, 1990), new AnnualEvent(1, JANUARY));
        assertThat(result).isPositive();
    }
}
