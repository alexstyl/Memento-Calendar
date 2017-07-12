package com.alexstyl.specialdates.contact;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class DisplayNameTest {

    @Test
    public void givenFirstName_thenDisplayNameHasOneFirstNameAndNoLastName() {
        DisplayName name = DisplayName.from("Alex");

        assertThat(name.getFirstNames().getCount()).isEqualTo(1);
        assertThat(name.getLastName()).isEmpty();
    }

    @Test
    public void givenFirstAndLastName_thenFirstNameIsCorrect() {
        DisplayName displayName = DisplayName.from("Alex Styl");
        assertThat(displayName.getFirstNames().getPrimary()).isEqualTo("Alex");
    }

    @Test
    public void givenFirstAndLastName_thenLastNameIsCorrect() {
        DisplayName displayName = DisplayName.from("Alex Styl");
        assertThat(displayName.getLastName()).isEqualTo("Styl");
    }

    @Test
    public void givenFirstAndLastName_thenDisplayNameHasOneFirstAndLastName() {
        DisplayName displayName = DisplayName.from("Alex Styl");

        assertThat(displayName.getFirstNames().getCount()).isEqualTo(1);
        assertThat(displayName.getLastName()).isNotEmpty();
    }

    @Test
    public void givenMultipleFirstNames_thenDisplayNameHasMultipleNamesAndLastName() {
        DisplayName displayName = DisplayName.from("John Peters Jackson");

        assertThat(displayName.hasMultipleFirstNames()).isTrue();
        assertThat(displayName.getFirstNames().getCount()).isEqualTo(2);
        assertThat(displayName.getLastName()).isNotEmpty();
    }

    @Test
    public void givenFirstNameWithSemicolumnAndLastName_thenDisplayNameHasTwoFirstNameAndLastName() {
        DisplayName displayName = DisplayName.from("John-Peters Jackson");

        assertThat(displayName.hasMultipleFirstNames()).isTrue();
        assertThat(displayName.getFirstNames().getCount()).isEqualTo(2);
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

    @Test
    public void toStringReturnsTheFullName() {
        String nameRaw = "Alex Styl";
        String toString = DisplayName.from(nameRaw).toString();
        assertThat(nameRaw).isEqualTo(toString);
    }

    private static List<String> asList(Names names) {
        List<String> namesList = new ArrayList<>();
        for (String name : names) {
            namesList.add(name);
        }
        return namesList;
    }
}
