package com.alexstyl.gsc

import org.fest.assertions.api.Assertions
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SoundTest {

    @Test
    fun combiningTwoSymbols_returnsASoundWithAllTheSymbols() {
        val result = Sound('A') + Sound('E')
        Assertions.assertThat(result).isEqualTo(Sound(charArrayOf('A', 'E')))
    }


    @Test
    fun appendingThreeSounds_returnsASoundWithAllSymbols() {
        var result = Sound.flatten(listOf(Sound('A'), Sound('B'), Sound('C')))
        Assertions.assertThat(result).isEqualTo(Sound("A,B,C"))
    }
}
