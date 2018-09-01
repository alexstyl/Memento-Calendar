package com.alexstyl.specialdates.timeofday

import com.alexstyl.specialdates.TimeOfDay
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TimeOfDayTest {


    @Test
    fun toStringReturnsTheSpecifiedLabel() {
        val timeOfDay = TimeOfDay.at(5, 0)
        assertThat(timeOfDay.toString()).isEqualTo("05:00")
    }

    @Test
    fun whenComparingALaterTimeToAnEarlierOne_thenReturnTrue() {
        assertThat(
                TimeOfDay.at(2, 0).isAfter(TimeOfDay.at(1, 0))
        ).isTrue
    }

    @Test
    fun whenComparingAnEarlierTimeToALaterOne_thenReturnFalse() {
        assertThat(
                TimeOfDay.at(1, 0).isAfter(TimeOfDay.at(2, 0))
        ).isFalse
    }

}
