package com.alexstyl.specialdates

import com.alexstyl.specialdates.contact.Names
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test

class NamesTest {

    @Test
    fun singleName() {
        val names = Names.parse("Πέτρος")

        assertThat(names).containsExactly("Πέτρος")
    }

    @Test
    fun singleNameWithNumbers() {
        val names = Names.parse("Π3τρος")

        assertThat(names).containsExactly("Π3τρος")
    }

    @Test
    fun twoNamesSplitWithSpace() {
        val names = Names.parse("Άννα Μαρία")

        assertThat(names).containsExactly("Άννα", "Μαρία")
    }

    @Test
    fun twoNamesSplitWithColumn() {
        val names = Names.parse("Άννα-Μαρία")

        assertThat(names).containsExactly("Άννα", "Μαρία")
    }

    @Test
    fun fiveNamesSpitWithSpacesAndColumns() {
        val names = Names.parse("Άννα Μαρία-Πέτρου Βασίλη Τάκη")

        assertThat(names).containsExactly("Άννα", "Μαρία", "Πέτρου", "Βασίλη", "Τάκη")
    }

    @Test
    fun emojisAreNotNames() {
        val names = Names.parse("~Alex~~<3❤️")

        assertThat(names).containsExactly("Alex", "3")
    }
}
