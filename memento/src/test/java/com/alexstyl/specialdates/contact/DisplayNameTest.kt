package com.alexstyl.specialdates.contact

import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test

class DisplayNameTest {

    @Test
    fun givenFirstName_thenDisplayNameHasOneFirstNameAndNoLastName() {
        val displayName = DisplayName.from("Alex")
        val firstNames = displayName.firstNames
        val lastName = displayName.lastName

        assertThat(firstNames.size).isEqualTo(1)
        assertThat(lastName).isEmpty()
    }

    @Test
    fun givenFirstAndLastName_thenFirstNameIsCorrect() {
        val firstNames = DisplayName.from("Alex Styl").firstNames
        assertThat(firstNames[0]).isEqualTo("Alex")
    }

    @Test
    fun givenFirstAndLastName_thenLastNameIsCorrect() {
        val lastName = DisplayName.from("Alex Styl").lastName
        assertThat(lastName).isEqualTo("Styl")
    }

    @Test
    fun givenFirstAndLastName_thenDisplayNameHasOneFirstAndLastName() {
        val displayName = DisplayName.from("Alex Styl")
        val firstNames = displayName.firstNames
        val lastName = displayName.lastName

        assertThat(firstNames.size).isEqualTo(1)
        assertThat(lastName).isNotEmpty
    }

    @Test
    fun givenMultipleFirstNames_thenDisplayNameHasMultipleNamesAndLastName() {
        val displayName = DisplayName.from("John Peters Jackson")

        assertThat(displayName.hasMultipleFirstNames()).isTrue
        assertThat(displayName.firstNames.size).isEqualTo(2)
        assertThat(displayName.lastName).isNotEmpty
    }

    @Test
    fun givenFirstNameWithSemicolumnAndLastName_thenDisplayNameHasTwoFirstNameAndLastName() {
        val displayName = DisplayName.from("John-Peters Jackson")

        assertThat(displayName.hasMultipleFirstNames()).isTrue
        assertThat(displayName.firstNames.size).isEqualTo(2)
        assertThat(displayName.lastName).isNotEmpty
    }

    @Test
    fun givenEmptyName_NoNameIsReturned() {
        val displayName = DisplayName.from("")

        assertThat(displayName).isEqualTo(NO_NAME)
    }

    @Test
    fun givenNullName_NoNameIsReturned() {
        val displayName = DisplayName.from(null)

        assertThat(displayName).isEqualTo(NO_NAME)
    }

    @Test
    fun noName_ReturnsEmptyName() {
        val displayName = NO_NAME
        assertThat(displayName.toString()).isEqualTo("")
    }

    @Test
    fun allNames() {
        val names = DisplayName.from("Alex Bob Derek Styl").allNames
        assertThat(names).contains("Alex", "Bob", "Derek", "Styl")
    }

    @Test
    fun toStringReturnsTheFullName() {
        val nameRaw = "Alex Styl"
        val toString = DisplayName.from(nameRaw).toString()
        assertThat(nameRaw).isEqualTo(toString)
    }

    companion object {
        private val NO_NAME = DisplayName("", emptyList(), emptyList(), "")
    }
}
