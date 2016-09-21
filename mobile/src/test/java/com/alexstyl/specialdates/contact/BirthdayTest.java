package com.alexstyl.specialdates.contact;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class BirthdayTest {

    @Test
    public void shortNameWithYear() {
        Birthday aBirthday = Birthday.on(1, 2, 1990);
        assertThat(aBirthday.toShortDate()).isEqualTo("--02-01");
    }

    @Test
    public void shortNameWithNoYear() {
        Birthday aBirthday = Birthday.on(1, 2);
        assertThat(aBirthday.toShortDate()).isEqualTo("--02-01");
    }

    @Test
    public void whenNoYearIsPresent_ItDoesNotIncludeYear() {
        Birthday birthday = Birthday.on(1, 2);
        assertThat(birthday.hasYearOfBirth()).isFalse();
    }

    @Test
    public void whenYearIsPresent_ItDoesIncludeYear() {
        Birthday birthday = Birthday.on(1, 2, 1990);
        assertThat(birthday.hasYearOfBirth()).isTrue();
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenInvalidYearInPassed_thenExceptionIsThrown() {
        Birthday.on(1, 2, -1);
    }
}
