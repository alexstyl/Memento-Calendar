package com.alexstyl.specialdates;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class DisplayNameTest {
    private static final String FIRST_NAME = "Alex";
    private static final String LAST_NAME = "Styl";

    private static final int ONE_FIRSTNAME = 1;

    private static final String FIRST_AND_LAST_NAME = FIRST_NAME + " " + LAST_NAME;
    private static final String TWO_FIRST_NAMES_AND_LASTNAME = "John Peters Jackson";
    private static final String TWO_FIRST_NAMES_AND_LASTNAME_WITH_SEMICOLUMN = "John-Peters Jackson";

    private static final int TWO_FIRSTNAMES = 2;

    @Test
    public void givenFirstName_thenDisplayNameHasOneFirstNameAndNoLastName() {
        DisplayName name = DisplayName.from(FIRST_NAME);

        assertThat(name.getFirstNames().getCount()).isEqualTo(ONE_FIRSTNAME);
        assertThat(name.getLastName()).isEmpty();
    }

    @Test
    public void givenFirstAndLastName_thenFirstNameIsCorrect() {
        DisplayName displayName = DisplayName.from(FIRST_AND_LAST_NAME);
        assertThat(displayName.getFirstNames().getPrimary()).isEqualTo(FIRST_NAME);
    }

    @Test
    public void givenFirstAndLastName_thenLastNameIsCorrect() {
        DisplayName displayName = DisplayName.from(FIRST_AND_LAST_NAME);
        assertThat(displayName.getLastName()).isEqualTo(LAST_NAME);
    }

    @Test
    public void givenFirstAndLastName_thenDisplayNameHasOneFirstAndLastName() {
        DisplayName displayName = DisplayName.from(FIRST_AND_LAST_NAME);

        assertThat(displayName.getFirstNames().getCount()).isEqualTo(ONE_FIRSTNAME);
        assertThat(displayName.getLastName()).isNotEmpty();
    }

    @Test
    public void givenMultipleFirstNames_thenDisplayNameHasMultipleNamesAndLastName() {
        DisplayName displayName = DisplayName.from(TWO_FIRST_NAMES_AND_LASTNAME);

        assertThat(displayName.hasMultipleFirstNames()).isTrue();
        assertThat(displayName.getFirstNames().getCount()).isEqualTo(TWO_FIRSTNAMES);
        assertThat(displayName.getLastName()).isNotEmpty();
    }

    @Test
    public void givenFirstNameWithSemicolumnAndLastName_thenDisplayNameHasTwoFirstNameAndLastName() {
        DisplayName displayName = DisplayName.from(TWO_FIRST_NAMES_AND_LASTNAME_WITH_SEMICOLUMN);

        assertThat(displayName.hasMultipleFirstNames()).isTrue();
        assertThat(displayName.getFirstNames().getCount()).isEqualTo(TWO_FIRSTNAMES);
        assertThat(displayName.getLastName()).isNotEmpty();
    }

    @Test
    public void givenEmptyName_NoNameIsReturned() {
        DisplayName displayName = DisplayName.from("");

        assertThat(displayName).isEqualTo(DisplayName.NO_NAME);
    }

    @Test
    public void givenNullName_NoNameIsReturned() {
        DisplayName displayName = DisplayName.from(null);

        assertThat(displayName).isEqualTo(DisplayName.NO_NAME);
    }

    @Test
    public void noName_ReturnsEmptyName() {
        DisplayName displayName = DisplayName.NO_NAME;
        assertThat(displayName.toString()).isEqualTo("");
    }

    @Test
    public void allNames() {
        DisplayName displayName = DisplayName.from("Alex Bob Derek Styl");
        Names names = displayName.getAllNames();
        List<String> namesList = asList(names);
        assertThat(namesList).contains("Alex", "Bob", "Derek", "Styl");
    }

    private static List<String> asList(Names names) {
        List<String> namesList = new ArrayList<>();
        for (String name : names) {
            namesList.add(name);
        }
        return namesList;
    }
}
