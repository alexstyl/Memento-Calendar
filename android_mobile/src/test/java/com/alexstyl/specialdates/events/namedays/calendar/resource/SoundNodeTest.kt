package com.alexstyl.specialdates.events.namedays.calendar.resource

import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.Months.AUGUST
import com.alexstyl.specialdates.date.Months.MARCH
import com.alexstyl.specialdates.date.Months.OCTOBER
import com.alexstyl.specialdates.events.namedays.NameCelebrations
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test

class SoundNodeTest {

    @Test
    fun simpleCase() {
        val node = SoundNode().apply {
            addDate("A", Date.on(30, AUGUST, 1990))
        }

        val returningDate = node.getDates("A")

        assertThat(returningDate).isEqualTo(NameCelebrations("A", Date.on(30, AUGUST, 1990)))
    }

    @Test
    fun whenAddingNameAndDateCombination_thenTheCorrectCombinationIsReturned() {
        val node = SoundNode().apply {
            addDate("Alex", Date.on(30, AUGUST, 1990))
        }

        val returningDate = node.getDates("Alex")

        assertThat(returningDate).isEqualTo(NameCelebrations("Alex", Date.on(30, AUGUST, 1990)))
    }

    @Test
    fun addingMultipleNames_thenReturnCorrectDates() {
        val node = SoundNode().apply {
            addDate("Alex", Date.on(30, AUGUST, 1990))
            addDate("Alexandros", Date.on(5, OCTOBER, 1990))
            addDate("Banana", Date.on(11, MARCH, 1990))
        }

        assertThat(node.getDates("Alex")).isEqualTo(NameCelebrations("Alex", Date.on(30, AUGUST, 1990)))
        assertThat(node.getDates("Alexandros")).isEqualTo(NameCelebrations("Alexandros", Date.on(5, OCTOBER, 1990)))
        assertThat(node.getDates("Banana")).isEqualTo(NameCelebrations("Banana", Date.on(11, MARCH, 1990)))
    }


    @Test
    fun whenAddingNameAndDateCombination_thenTheAskingFor() {
        val node = SoundNode().apply {
            addDate("Alex", Date.on(30, AUGUST, 1990))
        }

        val returningDate = node.getDates("Alexandros")

        assertThat(returningDate).isEqualTo(NameCelebrations("Alexandros"))
    }
}
