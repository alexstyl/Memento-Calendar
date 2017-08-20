package com.alexstyl.specialdates.events.namedays.calendar.resource

import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.Months.AUGUST
import com.alexstyl.specialdates.events.namedays.NameCelebrations
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test

class SoundNodeTest {

    @Test
    fun whenAddingNameAndDateCombination_thenTheCorrectCombinationIsReturned() {
        val node = SoundNode().apply {
            addDate("Alex", Date.on(30, AUGUST, 1990))
        }

        val returningDate = node.getDates("Alex")

        assertThat(returningDate).isEqualTo(NameCelebrations("Alex", Date.on(30, AUGUST, 1990)))
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
