package com.alexstyl.specialdates.events.namedays.calendar.resource;

import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.namedays.calendar.OrthodoxEasterCalculator;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class RomanianEasterSpecialCalculatorTest {

    private static final OrthodoxEasterCalculator easterCalculator = new OrthodoxEasterCalculator();

    @Test
    public void calculatesSundayBeforeEasterCorrectly() {
        RomanianEasterSpecialCalculator calculator = new RomanianEasterSpecialCalculator(easterCalculator);
        List<Date> expectedDates = buildExpectedDates();
        for (Date expectedDate : expectedDates) {
            Date actualDate = calculator.calculateSpecialRomanianDayForYear(expectedDate.getYear());
            assertThat(expectedDate).isEqualTo(actualDate);
        }
    }

    private List<Date> buildExpectedDates() {
        List<Date> dates = new ArrayList<>();
        dates.add(Date.Companion.on(9, 4, 2017));
        dates.add(Date.Companion.on(1, 4, 2018));
        dates.add(Date.Companion.on(21, 4, 2019));
        dates.add(Date.Companion.on(12, 4, 2020));
        dates.add(Date.Companion.on(25, 4, 2021));
        dates.add(Date.Companion.on(17, 4, 2022));
        return dates;
    }
}
