package com.alexstyl.specialdates.search

import com.alexstyl.specialdates.contact.DisplayName
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test

class NameMatcherTest {

    private val comparer = NameMatcher

    @Test
    fun rules() {
        comparer.match("Alex Styl".asName(), "Al").assertTrue()
        comparer.match("Alex Styl".asName(), "Αλ").assertTrue()
        comparer.match("Alex Styl".asName(), "St").assertTrue()
        comparer.match("Alex Styl".asName(), "Στ").assertTrue()
    }
}


private fun Boolean.assertTrue() {
    assertThat(this).isTrue
}

private fun String.asName() = DisplayName.from(this)
