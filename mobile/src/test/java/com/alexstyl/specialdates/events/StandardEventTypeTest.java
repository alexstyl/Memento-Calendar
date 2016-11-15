package com.alexstyl.specialdates.events;

import com.alexstyl.specialdates.events.database.EventColumns;
import com.alexstyl.specialdates.events.database.EventsDBContract.AnnualEventsContract;
import com.alexstyl.specialdates.events.peopleevents.StandardEventType;
import com.novoda.notils.exception.DeveloperError;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class StandardEventTypeTest {

    @Test
    public void mapsTypeBirthdayIdToBirthdayEvent() {
        StandardEventType eventType = StandardEventType.fromId(AnnualEventsContract.TYPE_BIRTHDAY);
        assertThat(eventType).isEqualTo(StandardEventType.BIRTHDAY);
    }

    @Test
    public void mapsTypeNamedayIdToNamedayEvent() {
        StandardEventType eventType = StandardEventType.fromId(AnnualEventsContract.TYPE_NAMEDAY);
        assertThat(eventType).isEqualTo(StandardEventType.NAMEDAY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwExceptionOnInvalidId() {
        StandardEventType.fromId(5);
    }

    @Test(expected = DeveloperError.class)
    public void throwsADeveloperErrorIfTriedtoCreateACustomEvent() {
        StandardEventType.fromId(EventColumns.TYPE_CUSTOM);
    }
}
