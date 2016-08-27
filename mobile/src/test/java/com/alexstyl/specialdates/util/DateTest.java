package com.alexstyl.specialdates.util;

import com.alexstyl.specialdates.events.DayDate;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class DateTest {


    @Test
    public void whenComparingToSameDate_thenTheyAreEqual() {
        DayDate firstDate = DayDate.newInstance(16, 4, 1991);
        DayDate secondDate = DayDate.newInstance(16, 4, 1991);

        assertThat(firstDate.equals(secondDate)).isTrue();
    }

    @Test
    public void whenComparingToDateWithDifferentYear_thenTheyAreNotEqual() {
        DayDate firstDate = DayDate.newInstance(16, 4, 1991);
        DayDate secondDate = DayDate.newInstance(16, 4, 1987);

        assertThat(firstDate.equals(secondDate)).isFalse();
    }

}
