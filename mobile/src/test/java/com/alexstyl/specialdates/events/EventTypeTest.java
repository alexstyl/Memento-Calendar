package com.alexstyl.specialdates.events;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class EventTypeTest {

    @Test
    public void mapsTypeBirthdayIdToBirthdayEvent() {
        EventType eventType = EventType.fromId(EventColumns.TYPE_BIRTHDAY);
        assertThat(eventType).isEqualTo(EventType.BIRTHDAY);
    }

    @Test
    public void mapsTypeNamedayIdToNamedayEvent() {
        EventType eventType = EventType.fromId(EventColumns.TYPE_NAMEDAY);
        assertThat(eventType).isEqualTo(EventType.NAMEDAY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwExceptionOnInvalidId() {
        EventType.fromId(3);
    }
}
