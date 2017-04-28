package com.alexstyl.specialdates.upcoming;

import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.TimePeriod;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static com.alexstyl.specialdates.date.DateConstants.FEBRUARY;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.fail;

@RunWith(MockitoJUnitRunner.class)
public class TimePeriodTest {

    @Test
    public void dateBetweenPeriod() {
        TimePeriod range = TimePeriod.between(
                Date.startOfTheYear(2016),
                Date.startOfTheYear(2017)
        );
        assertThat(range.containsDate(Date.on(1, FEBRUARY, 2016))).isTrue();
    }

    @Test
    public void dateOutsideOfPeriod() {
        TimePeriod range = TimePeriod.between(
                Date.startOfTheYear(2016),
                Date.startOfTheYear(2017)
        );
        assertThat(range.containsDate(Date.on(1, FEBRUARY, 1990))).isFalse();
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidPeriodThrowsException() {
        TimePeriod.between(
                Date.startOfTheYear(3560),
                Date.startOfTheYear(5)
        );
        fail("Should have thrown exception");
    }
}
