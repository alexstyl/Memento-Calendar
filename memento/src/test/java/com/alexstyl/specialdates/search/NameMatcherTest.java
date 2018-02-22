package com.alexstyl.specialdates.search;

import com.alexstyl.specialdates.contact.DisplayName;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class NameMatcherTest {

    private static final String FULL_DISPLAY_NAME = "Alex Styl";
    private static final String PART_OF_FULL_NAME = "Alex St";
    private static final DisplayName DISPLAY_NAME = DisplayName.Companion.from(FULL_DISPLAY_NAME);

    private static final String FIRST_LETTER_OF_FIRST_NAME = "A";
    private static final String FIRST_LETTER_OF_LAST_NAME = "S";

    private NameMatcher comparer = NameMatcher.INSTANCE;

    @Test
    public void testFirstLetterOfFirstName_returnsTrue() {
        boolean matches = givenDisplayNameMatches(FIRST_LETTER_OF_FIRST_NAME);
        assertThat(matches).isTrue();
    }

    @Test
    public void testFirstLetterOfLastName_returnsTrue() {
        boolean matches = givenDisplayNameMatches(FIRST_LETTER_OF_LAST_NAME);
        assertThat(matches).isTrue();
    }

    @Test
    public void testFullDisplayName_returnsTrue() {
        boolean matches = givenDisplayNameMatches(FULL_DISPLAY_NAME);
        assertThat(matches).isTrue();
    }

    @Test
    public void testPartOfFullName_returnsTrue() {
        boolean matches = givenDisplayNameMatches(PART_OF_FULL_NAME);
        assertThat(matches).isTrue();
    }

    private boolean givenDisplayNameMatches(String query) {
        return comparer.match(DISPLAY_NAME, query);
    }
}
