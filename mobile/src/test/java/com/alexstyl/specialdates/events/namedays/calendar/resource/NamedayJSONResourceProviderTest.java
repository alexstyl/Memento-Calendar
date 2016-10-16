package com.alexstyl.specialdates.events.namedays.calendar.resource;

import com.alexstyl.specialdates.events.namedays.NamedayLocale;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class NamedayJSONResourceProviderTest {

    private NamedayJSONResourceProvider provider;

    @Before
    public void setUp() throws Exception {
        provider = new NamedayJSONResourceProvider(new JavaJSONResourceLoader());
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
        NamedayJSON namedays = provider.getNamedayJSONFor(NamedayLocale.gr);
        hasSpecial(namedays);
    }

    @Test
    public void roHasSpecial() throws Exception {
        NamedayJSON namedays = provider.getNamedayJSONFor(NamedayLocale.ro);
        hasSpecial(namedays);
    }

    @Test
    public void ruHasNoSpecial() throws Exception {
        NamedayJSON namedays = provider.getNamedayJSONFor(NamedayLocale.ru);
        hasNoSpecial(namedays);
    }

    @Test
    public void lvHasNoSpecial() throws Exception {
        NamedayJSON namedays = provider.getNamedayJSONFor(NamedayLocale.lv);
        hasNoSpecial(namedays);
    }

    @Test
    public void csHasNoSpecial() throws Exception {
        NamedayJSON namedays = provider.getNamedayJSONFor(NamedayLocale.cs);
        hasNoSpecial(namedays);
    }

    @Test
    public void skHasNoSpecial() throws Exception {
        NamedayJSON namedays = provider.getNamedayJSONFor(NamedayLocale.sk);
        hasNoSpecial(namedays);
    }

    private void hasSpecial(NamedayJSON namedays) {
        assertThat(namedays.getSpecial().length()).isNotZero();
    }

    private void hasNoSpecial(NamedayJSON namedays) {
        assertThat(namedays.getSpecial().length()).isZero();
    }
}
