package com.alexstyl.specialdates.events;

import com.alexstyl.specialdates.events.database.EventsDBContract.AnnualEventsContract;
import com.alexstyl.specialdates.events.peopleevents.EventType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class EventTypeTest {

    @Test
    public void mapsTypeBirthdayIdToBirthdayEvent() {
        EventType eventType = EventType.fromId(AnnualEventsContract.TYPE_BIRTHDAY);
        assertThat(eventType).isEqualTo(EventType.BIRTHDAY);
    }

    @Test
    public void mapsTypeNamedayIdToNamedayEvent() {
        EventType eventType = EventType.fromId(AnnualEventsContract.TYPE_NAMEDAY);
        assertThat(eventType).isEqualTo(EventType.NAMEDAY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwExceptionOnInvalidId() {
        EventType.fromId(3);
    }
}
