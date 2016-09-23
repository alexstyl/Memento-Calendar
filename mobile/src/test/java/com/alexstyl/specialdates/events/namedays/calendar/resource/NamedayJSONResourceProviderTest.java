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
    public void namedaysAreNotEmpty() throws Exception {
        NamedayJSON namedays = provider.getNamedayJSONFor(NamedayLocale.gr);
        assertThat(namedays.getData()).isNotNull();
        assertThat(namedays.getSpecial()).isNotNull();
    }
}
