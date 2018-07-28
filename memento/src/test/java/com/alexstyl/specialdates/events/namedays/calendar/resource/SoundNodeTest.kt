package com.alexstyl.specialdates.events.namedays.calendar.resource

import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.Months.AUGUST
import com.alexstyl.specialdates.date.Months.MARCH
import com.alexstyl.specialdates.date.Months.OCTOBER
import com.alexstyl.specialdates.events.namedays.ImmutableNameCelebrations
import com.alexstyl.specialdates.events.namedays.NoNameCelebrations
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test

class SoundNodeTest {

    @Test
    fun simpleCase() {
        val node = SoundNode().apply {
            addDate("A", Date.on(30, AUGUST, 1990))
        }

        val returningDate = node.getDates("A")

        assertThat(returningDate).isEqualTo(ImmutableNameCelebrations("A", listOf(Date.on(30, AUGUST, 1990))))
    }

    @Test
    fun whenAddingNameAndDateCombination_thenTheCorrectCombinationIsReturned() {
        val node = SoundNode()
        node.addDate("Alex", Date.on(30, AUGUST, 1990))

        val returningDate = node.getDates("Alex")

        assertThat(returningDate).isEqualTo(ImmutableNameCelebrations("Alex", listOf(Date.on(30, AUGUST, 1990))))
    }

    @Test
    fun addingMultipleNames_thenReturnCorrectDates() {
        val node = SoundNode().apply {
            addDate("Alex", Date.on(30, AUGUST, 1990))
            addDate("Alexandros", Date.on(5, OCTOBER, 1990))
            addDate("Banana", Date.on(11, MARCH, 1990))
        }
        assertThat(node.getDates("Alex")).isEqualTo(ImmutableNameCelebrations("Alex", listOf(Date.on(30, AUGUST, 1990))))
        assertThat(node.getDates("Alexandros")).isEqualTo(ImmutableNameCelebrations("Alexandros", listOf(Date.on(5, OCTOBER, 1990))))
        assertThat(node.getDates("Banana")).isEqualTo(ImmutableNameCelebrations("Banana", listOf(Date.on(11, MARCH, 1990))))
    }


    @Test
    fun whenAddingNameAndDateCombination_thenTheAskingFor() {
        val node = SoundNode().apply {
            addDate("Alex", Date.on(30, AUGUST, 1990))
        }

        val returningDate = node.getDates("Alexandros")

        assertThat(returningDate).isEqualTo(NoNameCelebrations("Alexandros"))
    }
}
