package com.alexstyl.specialdates.upcoming;

import com.alexstyl.specialdates.date.TimePeriod;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static com.alexstyl.specialdates.date.DateExtKt.dateOn;
import static com.alexstyl.specialdates.date.DateExtKt.beggingOfYear;
import static com.alexstyl.specialdates.date.Months.FEBRUARY;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.fail;

@RunWith(MockitoJUnitRunner.class)
public class TimePeriodTest {

    @Test
    public void dateBetweenPeriod() {
        TimePeriod range = TimePeriod.Companion.between(
                beggingOfYear(2016),
                beggingOfYear(2017)
        );
        assertThat(range.containsDate(dateOn(1, FEBRUARY, 2016))).isTrue();
    }

    @Test
    public void dateOutsideOfPeriod() {
        TimePeriod range = TimePeriod.Companion.between(
                beggingOfYear(2016),
                beggingOfYear(2017)
        );
        assertThat(range.containsDate(dateOn(1, FEBRUARY, 1990))).isFalse();
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidPeriodThrowsException() {
        TimePeriod.Companion.between(
                beggingOfYear(3560),
                beggingOfYear(5)
        );
        fail("Should have thrown exception");
    }
}
