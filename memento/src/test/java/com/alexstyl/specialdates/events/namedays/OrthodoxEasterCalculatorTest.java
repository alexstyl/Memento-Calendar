package com.alexstyl.specialdates.events.namedays;

import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.namedays.calendar.OrthodoxEasterCalculator;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static com.alexstyl.specialdates.date.DateExtKt.dateOn;
import static com.alexstyl.specialdates.date.Months.APRIL;
import static com.alexstyl.specialdates.date.Months.MAY;
import static org.fest.assertions.api.Assertions.assertThat;

public class OrthodoxEasterCalculatorTest {

    private final HashMap<Integer, Date> EXPECTED_DATES = new HashMap<>();
    private final OrthodoxEasterCalculator calculator = new OrthodoxEasterCalculator();

    @Before
    public void initExpectedDates() {
        addDay(2010, dateOn(4, APRIL, 2010));
        addDay(2011, dateOn(24, APRIL, 2011));
        addDay(2012, dateOn(15, APRIL, 2012));
        addDay(2013, dateOn(5, MAY, 2013));
        addDay(2014, dateOn(20, APRIL, 2014));
        addDay(2015, dateOn(12, APRIL, 2015));
        addDay(2016, dateOn(1, MAY, 2016));
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
            assertThatCalendarsReferToTheSameDate(expectedDate, actualDate);
        }
    }

    private void assertThatCalendarsReferToTheSameDate(Date expectedDate, Date actualDate) {
        assertThat(expectedDate.getYear()).isEqualTo(actualDate.getYear());
        assertThat(expectedDate.getMonth()).isEqualTo(actualDate.getMonth());
        assertThat(expectedDate.getDayOfMonth()).isEqualTo(actualDate.getDayOfMonth());
    }
}
