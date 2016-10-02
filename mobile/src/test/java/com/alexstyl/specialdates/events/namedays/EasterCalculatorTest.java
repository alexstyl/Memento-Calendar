package com.alexstyl.specialdates.events.namedays;

import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.namedays.calendar.EasterCalculator;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import static com.alexstyl.specialdates.date.DateConstants.APRIL;
import static com.alexstyl.specialdates.date.DateConstants.MAY;
import static org.fest.assertions.api.Assertions.assertThat;

public class EasterCalculatorTest {

    private HashMap<Integer, Date> EXPECTED_DATES = new HashMap<>();
    private final EasterCalculator calculator = new EasterCalculator();

    @Before
    public void initExpectedDates() {
        addDay(2010, Date.on(4, APRIL, 2010));
        addDay(2011, Date.on(24, APRIL, 2011));
        addDay(2012, Date.on(15, APRIL, 2012));
        addDay(2013, Date.on(5, MAY, 2013));
        addDay(2014, Date.on(20, APRIL, 2014));
        addDay(2015, Date.on(12, APRIL, 2015));
        addDay(2016, Date.on(1, MAY, 2016));
    }

    private void addDay(int year, Date date) {
        EXPECTED_DATES.put(year, date);
    }

    @Test
    public void easternDatesAreCalcualtedProperly() {
        for (int year : EXPECTED_DATES.keySet()) {
            Date expectedDate = EXPECTED_DATES.get(year);
            Date actualDate = calculator.calculateEasterForYear(year);
            assertThat(actualDate).isEqualTo(expectedDate);
//            assertThatCalendarsReferToTheSameDate(expectedDate, actualDate);
        }
    }

    private void assertThatCalendarsReferToTheSameDate(Date expectedDate, Date actualDate) {
        assertThat(expectedDate.getYear()).isEqualTo(actualDate.getYear());
        assertThat(expectedDate.getMonth()).isEqualTo(actualDate.getMonth());
        assertThat(expectedDate.getDayOfMonth()).isEqualTo(actualDate.getDayOfMonth());
    }
}
