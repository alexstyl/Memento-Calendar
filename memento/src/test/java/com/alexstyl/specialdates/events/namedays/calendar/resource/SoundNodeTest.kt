package com.alexstyl.specialdates.events.namedays.calendar.resource

import com.alexstyl.specialdates.date.Months.AUGUST
import com.alexstyl.specialdates.date.Months.MARCH
import com.alexstyl.specialdates.date.Months.OCTOBER
import com.alexstyl.specialdates.date.dateOn
import com.alexstyl.specialdates.events.namedays.ImmutableNameCelebrations
import com.alexstyl.specialdates.events.namedays.NoCelebrations
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test

class SoundNodeTest {

    @Test
    fun simpleCase() {
        val node = SoundNode().apply {
            addDate("A", dateOn(30, AUGUST, 1990))
        }

        val returningDate = node.getDates("A")

        assertThat(returningDate).isEqualTo(ImmutableNameCelebrations("A", listOf(dateOn(30, AUGUST, 1990))))
    }

    @Test
    fun whenAddingNameAndDateCombination_thenTheCorrectCombinationIsReturned() {
        val node = SoundNode()
        node.addDate("Alex", dateOn(30, AUGUST, 1990))

        val returningDate = node.getDates("Alex")

        assertThat(returningDate).isEqualTo(ImmutableNameCelebrations("Alex", listOf(dateOn(30, AUGUST, 1990))))
    }

    @Test
    fun addingMultipleNames_thenReturnCorrectDates() {
        val node = SoundNode().apply {
            addDate("Alex", dateOn(30, AUGUST, 1990))
            addDate("Alexandros", dateOn(5, OCTOBER, 1990))
            addDate("Banana", dateOn(11, MARCH, 1990))
        }
        assertThat(node.getDates("Alex")).isEqualTo(ImmutableNameCelebrations("Alex", listOf(dateOn(30, AUGUST, 1990))))
        assertThat(node.getDates("Alexandros")).isEqualTo(ImmutableNameCelebrations("Alexandros", listOf(dateOn(5, OCTOBER, 1990))))
        assertThat(node.getDates("Banana")).isEqualTo(ImmutableNameCelebrations("Banana", listOf(dateOn(11, MARCH, 1990))))
    }


    @Test
    fun whenAddingNameAndDateCombination_thenTheAskingFor() {
        val node = SoundNode().apply {
            addDate("Alex", dateOn(30, AUGUST, 1990))
        }

        val returningDate = node.getDates("Alexandros")

        assertThat(returningDate).isEqualTo(NoCelebrations("Alexandros"))
    }
}
