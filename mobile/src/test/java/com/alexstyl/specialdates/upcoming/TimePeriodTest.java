package com.alexstyl.specialdates.upcoming;

import com.alexstyl.specialdates.date.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static com.alexstyl.specialdates.date.DateConstants.FEBRUARY;
import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class TimePeriodTest {

    @Test
    public void dateBetweenPeriod() {
        TimePeriod range = new TimePeriod(
                Date.startOfTheYear(2016),
                Date.startOfTheYear(2017)
        );
        assertThat(range.containsDate(Date.on(1, FEBRUARY, 2016))).isTrue();
    }

    @Test
    public void dateOutsideOfPeriod() {
        TimePeriod range = new TimePeriod(
                Date.startOfTheYear(2016),
                Date.startOfTheYear(2017)
        );
        assertThat(range.containsDate(Date.on(1, FEBRUARY, 1990))).isFalse();
    }
}
