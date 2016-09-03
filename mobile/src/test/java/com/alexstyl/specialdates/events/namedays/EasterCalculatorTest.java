package com.alexstyl.specialdates.events.namedays;

import com.alexstyl.specialdates.events.DayDate;
import com.alexstyl.specialdates.events.namedays.calendar.EasterCalculator;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class EasterCalculatorTest {

    private HashMap<Integer, DayDate> EXPECTED_DATES = new HashMap<>();

    @Before
    public void initExpectedDates() {
        addDay(2010, DayDate.newInstance(4, DayDate.APRIL, 2010));
        addDay(2011, DayDate.newInstance(24, DayDate.APRIL, 2011));
        addDay(2012, DayDate.newInstance(15, DayDate.APRIL, 2012));
        addDay(2013, DayDate.newInstance(5, DayDate.MAY, 2013));
        addDay(2014, DayDate.newInstance(20, DayDate.APRIL, 2014));
        addDay(2015, DayDate.newInstance(12, DayDate.APRIL, 2015));
        addDay(2016, DayDate.newInstance(1, DayDate.MAY, 2016));
    }

    private void addDay(int year, DayDate date) {
        EXPECTED_DATES.put(year, date);
    }

    @Test
    public void easternDatesAreCalcualtedProperly() {
        EasterCalculator calculator = new EasterCalculator();

        for (int year : EXPECTED_DATES.keySet()) {

            DayDate expectedDate = EXPECTED_DATES.get(year);
            DayDate actualDate = calculator.calculateEasterForYear(year);
            assertThatCalendarsReferToTheSameDate(expectedDate, actualDate);
        }
    }

    private void assertThatCalendarsReferToTheSameDate(DayDate expectedDate, DayDate actualDate) {
        assertThat(expectedDate.getYear()).isEqualTo(actualDate.getYear());
        assertThat(expectedDate.getMonth()).isEqualTo(actualDate.getMonth());
        assertThat(expectedDate.getDayOfMonth()).isEqualTo(actualDate.getDayOfMonth());
    }
}
