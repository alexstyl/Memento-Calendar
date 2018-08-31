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
    fun whenComparingSameTime_thenReturnsEquals() {
        val a = TimeOfDay.at(1, 0)
        val b = TimeOfDay.at(1, 0)
        assertThat(a == b).isTrue
    }

    @Test
    fun whenComparingDifferentTime_thenReturnsCorrectResults() {
        assertThat(
                TimeOfDay.at(2, 0) > TimeOfDay.at(1, 0)
        ).isTrue

        assertThat(
                TimeOfDay.at(2, 5) > TimeOfDay.at(2, 0)
        ).isTrue

        assertThat(
                TimeOfDay.at(1, 0) < TimeOfDay.at(5, 0)
        ).isTrue

        assertThat(
                TimeOfDay.at(1, 0) < TimeOfDay.at(1, 5)
        ).isTrue
    }

}
