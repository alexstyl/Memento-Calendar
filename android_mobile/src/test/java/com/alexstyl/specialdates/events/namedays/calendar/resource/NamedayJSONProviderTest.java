package com.alexstyl.specialdates.events.namedays.calendar.resource;

import com.alexstyl.specialdates.events.namedays.NamedayLocale;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class NamedayJSONProviderTest {

    private NamedayJSONProvider provider;

    @Before
    public void setUp() throws Exception {
        provider = new NamedayJSONProvider(new JavaJSONResourceLoader());
    }

    @Test
    public void allLocalesHaveData() throws Exception {
        for (NamedayLocale namedayLocale : NamedayLocale.values()) {
            NamedayJSON namedays = provider.getNamedayJSONFor(namedayLocale);
            assertThat(namedays.getData().length()).isNotZero();
        }
    }

    @Test
    public void grHasSpecial() throws Exception {
        NamedayJSON namedays = provider.getNamedayJSONFor(NamedayLocale.GREEK);
        hasSpecial(namedays);
    }

    @Test
    public void roHasSpecial() throws Exception {
        NamedayJSON namedays = provider.getNamedayJSONFor(NamedayLocale.ROMANIAN);
        hasSpecial(namedays);
    }

    @Test
    public void ruHasNoSpecial() throws Exception {
        NamedayJSON namedays = provider.getNamedayJSONFor(NamedayLocale.RUSSIAN);
        hasNoSpecial(namedays);
    }

    @Test
    public void lvHasNoSpecial() throws Exception {
        NamedayJSON namedays = provider.getNamedayJSONFor(NamedayLocale.LATVIAN);
        hasNoSpecial(namedays);
    }

    @Test
    public void csHasNoSpecial() throws Exception {
        NamedayJSON namedays = provider.getNamedayJSONFor(NamedayLocale.CZECH);
        hasNoSpecial(namedays);
    }

    @Test
    public void skHasNoSpecial() throws Exception {
        NamedayJSON namedays = provider.getNamedayJSONFor(NamedayLocale.SLOVAK);
        hasNoSpecial(namedays);
    }

    private void hasSpecial(NamedayJSON namedays) {
        assertThat(namedays.getSpecial().length()).isNotZero();
    }

    private void hasNoSpecial(NamedayJSON namedays) {
        assertThat(namedays.getSpecial().length()).isZero();
    }
}
