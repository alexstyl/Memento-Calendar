package com.alexstyl.specialdates


import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test

class PhoneticComparatorTest {

    private val comparator = PhoneticComparator()

    @Test
    fun comparingTheSameStringReturnsTrue() {
        assertThat("Αλέξανδρος".beginsWith("Αλέξανδρος")).isTrue
    }

    @Test
    fun partOfNameWithAccentsReturnsTrue() {
        assertThat("Αλέξανδρος".beginsWith("Αλέ")).isTrue
    }

    @Test
    fun comparingPartOfNameInDifferentAlphabetReturnsTrue() {
        assertThat("Αλέξανδρος".beginsWith("Αle")).isTrue
    }

    @Test
    fun englishName() {
        assertThat("Alexandros".beginsWith("Alexandros")).isTrue
    }
    @Test
    fun partOfEnglishName() {
        assertThat("Alexandros".beginsWith("Αle")).isTrue
    }


    private fun String.beginsWith(other: String): Boolean {
        return comparator.startsWith(this, other)
    }
}