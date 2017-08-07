package com.alexstyl.specialdates.person

import com.alexstyl.specialdates.date.Date
import com.alexstyl.specialdates.date.Months
import org.fest.assertions.api.Assertions
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.runners.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class StarSignTest{
    @Test
    fun aquarius() {
        var resultingStarSign = StarSign.forDateOfBirth(Date.on(1, Months.FEBRUARY))
        Assertions.assertThat(resultingStarSign).isEqualTo(StarSign.AQUARIUS)
    }

    @Test
    fun pisces() {
        var resultingStarSign = StarSign.forDateOfBirth(Date.on(1, Months.MARCH))
        Assertions.assertThat(resultingStarSign).isEqualTo(StarSign.PISCES)
    }

    @Test
    fun aries() {
        var resultingStarSign = StarSign.forDateOfBirth(Date.on(19, Months.APRIL))
        Assertions.assertThat(resultingStarSign).isEqualTo(StarSign.ARIES)
    }

    @Test
    fun taurus() {
        var resultingStarSign = StarSign.forDateOfBirth(Date.on(30, Months.APRIL))
        Assertions.assertThat(resultingStarSign).isEqualTo(StarSign.TAURUS)
    }

    @Test
    fun gemini() {
        var resultingStarSign = StarSign.forDateOfBirth(Date.on(19, Months.JUNE))
        Assertions.assertThat(resultingStarSign).isEqualTo(StarSign.GEMINI)
    }

    @Test
    fun cancer() {
        var resultingStarSign = StarSign.forDateOfBirth(Date.on(20, Months.JULY))
        Assertions.assertThat(resultingStarSign).isEqualTo(StarSign.CANCER)
    }

    @Test
    fun leo() {
        var resultingStarSign = StarSign.forDateOfBirth(Date.on(19, Months.AUGUST))
        Assertions.assertThat(resultingStarSign).isEqualTo(StarSign.LEO)
    }

    @Test
    fun virgo() {
        var resultingStarSign = StarSign.forDateOfBirth(Date.on(23, Months.AUGUST))
        Assertions.assertThat(resultingStarSign).isEqualTo(StarSign.VIRGO)
    }

    @Test
    fun libra() {
        var resultingStarSign = StarSign.forDateOfBirth(Date.on(23, Months.SEPTEMBER))
        Assertions.assertThat(resultingStarSign).isEqualTo(StarSign.LIBRA)
    }

    @Test
    fun scorpio() {
        var resultingStarSign = StarSign.forDateOfBirth(Date.on(5, Months.NOVEMBER))
        Assertions.assertThat(resultingStarSign).isEqualTo(StarSign.SCORPIO)
    }

    @Test
    fun sagittarius() {
        var resultingStarSign = StarSign.forDateOfBirth(Date.on(19, Months.DECEMBER))
        Assertions.assertThat(resultingStarSign).isEqualTo(StarSign.SAGITTARIUS)
    }


    @Test
    fun capricorn() {
        var resultingStarSign = StarSign.forDateOfBirth(Date.on(8, Months.JANUARY))
        Assertions.assertThat(resultingStarSign).isEqualTo(StarSign.CAPRICORN)
    }
}
