package com.alexstyl.specialdates.events.namedays.calendar;

import com.alexstyl.specialdates.date.DayDate;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class RomanianEasterSpecialCalculatorTest {

    @Test
    public void calculatesSundayBeforeEasterCorrectly() {
        RomanianEasterSpecialCalculator calculator = new RomanianEasterSpecialCalculator();
        List<DayDate> expectedDates = buildExpectedDates();
        for (DayDate expectedDate : expectedDates) {
            DayDate actualDate = calculator.calculateSpecialRomanianDayForYear(expectedDate.getYear());
            assertThat(expectedDate).isEqualTo(actualDate);
        }
    }

    private List<DayDate> buildExpectedDates() {
        List<DayDate> dayDates = new ArrayList<>();
        dayDates.add(DayDate.newInstance(9, 4, 2017));
        dayDates.add(DayDate.newInstance(1, 4, 2018));
        dayDates.add(DayDate.newInstance(21, 4, 2019));
        dayDates.add(DayDate.newInstance(12, 4, 2020));
        dayDates.add(DayDate.newInstance(25, 4, 2021));
        dayDates.add(DayDate.newInstance(17, 4, 2022));
        return dayDates;
    }
}
