package com.alexstyl.specialdates.events.namedays.activity

import org.junit.Assert
import org.junit.Test

class CheatNameTest {

    @Test
    fun name() {
        if (CheatName("Al3x") != CheatName("Alex")) {
            Assert.fail("names are different")
        }
    }

}
