package com.alexstyl.specialdates;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class NamesTest {

    private static final int ONE_NAME = 1;
    private static final int TWO_NAMES = 2;
    private static final String SINGLE_NAME = "Πέτρος";

    private static final String TWO_NAMES_WITH_SEMICOLUMN = "Άννα-Μαρία";
    private static final String TWO_NAMES_WITH_SPACE = "Άννα Μαρία";

    private static final String A_LOT_OF_NAMES = "Άννα Μαρία-Πέτρου Βασίλη Τάκη";
    private static final int FIVE_NAMES = 5;

    @Test
    public void givenOneName_thenFirstNamesAreOne() {
        Names names = Names.from(SINGLE_NAME);

        assertThat(names.getCount()).isGreaterThanOrEqualTo(ONE_NAME);
    }

    @Test
    public void givenTwoFirstName_thenFirstNameAreTwo() {
        Names names = Names.from(TWO_NAMES_WITH_SPACE);

        assertThat(names.getCount()).isGreaterThanOrEqualTo(TWO_NAMES);
    }

    @Test
    public void givenTwoFirstNameWithColumn_thenFirstNameCountIsCorrect() {
        Names names = Names.from(TWO_NAMES_WITH_SEMICOLUMN);

        assertThat(names.getCount()).isGreaterThanOrEqualTo(TWO_NAMES);
    }

    @Test
    public void givenManyFirstNames_thenFirstNameCountIsCorrect() {
        Names names = Names.from(A_LOT_OF_NAMES);

        assertThat(names.getCount()).isGreaterThanOrEqualTo(FIVE_NAMES);
    }
}
