package com.alexstyl.specialdates;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class OptionalTest {

    @Test
    public void presentNotEqualToAbsent() {
        Optional<String> one = new Optional<>("Hi");
        assertThat(one).isNotEqualTo(Optional.<String>absent());
    }

    @Test
    public void presentsAreEqual() {
        assertThat(new Optional<>("Hi")).isEqualTo(new Optional<>("Hi"));
    }

    @Test
    public void absentsAreEqual() {
        assertThat(Optional.<String>absent()).isEqualTo(Optional.<String>absent());
    }

}
