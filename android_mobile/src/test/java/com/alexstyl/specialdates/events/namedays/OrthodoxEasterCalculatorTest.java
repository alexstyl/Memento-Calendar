package com.alexstyl.specialdates.events.namedays;

import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.namedays.calendar.OrthodoxEasterCalculator;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import static com.alexstyl.specialdates.date.Months.APRIL;
import static com.alexstyl.specialdates.date.Months.MAY;
import static org.fest.assertions.api.Assertions.assertThat;

public class OrthodoxEasterCalculatorTest {

    private final HashMap<Integer, Date> EXPECTED_DATES = new HashMap<>();
    private final OrthodoxEasterCalculator calculator = OrthodoxEasterCalculator.INSTANCE;

    @Before
    public void initExpectedDates() {
        addDay(2010, Date.Companion.on(4, APRIL, 2010));
        addDay(2011, Date.Companion.on(24, APRIL, 2011));
        addDay(2012, Date.Companion.on(15, APRIL, 2012));
        addDay(2013, Date.Companion.on(5, MAY, 2013));
        addDay(2014, Date.Companion.on(20, APRIL, 2014));
        addDay(2015, Date.Companion.on(12, APRIL, 2015));
        addDay(2016, Date.Companion.on(1, MAY, 2016));
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
