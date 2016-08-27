package com.alexstyl.specialdates.events;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class DayDateTest {

    private static final int DAY = 5;
    private static final int MONTH = 10;
    private static final DayDate ANY_DATE = DayDate.newInstance(DAY, MONTH, 1990);

    @Test
    public void whenAddingADayOfMonth_thenIncreaseNormally() {

        DayDate after = ANY_DATE.addDay(1);
        int expectedDay = DAY + 1;

        assertThat(after.getDayOfMonth()).isEqualTo(expectedDay);
    }

    @Test(expected = Exception.class)
    public void tests() {
        DayDate dayDate = DayDate.newInstance(32, 3, 2016);
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
        DayDate lastDayOfYear = DayDate.newInstance(31, 12, 1990);
        DayDate firstDayOfNextYear = lastDayOfYear.addDay(1);

        assertThat(firstDayOfNextYear.getMonth()).isEqualTo(1);
        assertThat(firstDayOfNextYear.getDayOfMonth()).isEqualTo(1);
        int nextYear = lastDayOfYear.getYear() + 1;
        assertThat(firstDayOfNextYear.getYear()).isEqualTo(nextYear);
    }

    @Test
    public void whenNewYearMinus() {
        DayDate firstDayOfYear = DayDate.newInstance(1, 1, 1990);
        DayDate firstDayOfPreviousMonth = firstDayOfYear.minusMonth(1);

        assertThat(firstDayOfPreviousMonth.getMonth()).isEqualTo(12);
    }

    @Test
    public void testOneDayAhead() throws Exception {
        DayDate firstDayOfYear = DayDate.newInstance(1, 1, 1990);
        DayDate secondDayOfYear = DayDate.newInstance(2, 1, 1990);
        assertThat(firstDayOfYear.daysDifferenceTo(secondDayOfYear)).isEqualTo(1);
    }

    @Test
    public void testOneYearAhead() throws Exception {
        DayDate firstDayOfYear = DayDate.newInstance(1, 1, 1990);
        DayDate secondDayOfYear = DayDate.newInstance(1, 1, 1991);
        assertThat(firstDayOfYear.daysDifferenceTo(secondDayOfYear)).isEqualTo(365);
    }

    @Test
    public void testTwoYearAhead() throws Exception {
        DayDate firstDayOfYear = DayDate.newInstance(1, 1, 1990);
        DayDate secondDayOfYear = DayDate.newInstance(1, 1, 1992);
        assertThat(firstDayOfYear.daysDifferenceTo(secondDayOfYear)).isEqualTo(365 * 2);
    }
}
