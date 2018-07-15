package com.alexstyl.specialdates.timeofday;

import com.alexstyl.specialdates.TimeOfDay;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class TimeOfDayTest {

    @Test
    public void toStringReturnsTheSpecifiedLabel() {
        TimeOfDay timeOfDay = TimeOfDay.Companion.at(5, 0);
        assertThat(timeOfDay.toString()).isEqualTo("05:00");
    }
}
