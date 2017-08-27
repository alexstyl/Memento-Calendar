package com.alexstyl.gsc

import org.fest.assertions.api.Assertions
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SoundTest {

    @Test
    fun combiningTwoSymbols_returnsASoundWithAllTheSymbols() {
        val result = sound('A') + sound('E')
        Assertions.assertThat(result).isEqualTo(sound(charArrayOf('A', 'E')))
    }


    @Test
    fun appendingThreeSounds_returnsASoundWithAllSymbols() {
        var result = Sound.flatten(listOf(sound('A'), sound('B'), sound('C')))
        Assertions.assertThat(result).isEqualTo(sound("A,B,C"))
    }
}
