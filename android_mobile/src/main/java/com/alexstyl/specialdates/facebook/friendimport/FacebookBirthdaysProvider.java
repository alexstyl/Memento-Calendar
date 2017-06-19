package com.alexstyl.specialdates.facebook.friendimport;

import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.DateParseException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import static com.alexstyl.specialdates.facebook.friendimport.StreamUtil.closeStream;

class FacebookBirthdaysProvider {

    private final CalendarLoader calendarLoader;
    private final ContactEventSerialiser serialiser;

    FacebookBirthdaysProvider(CalendarLoader calendarLoader, ContactEventSerialiser serialiser) {
        this.calendarLoader = calendarLoader;
        this.serialiser = serialiser;
    }

    List<ContactEvent> fetchCalendarFrom(URL url) throws CalendarFetcherException {
        InputStream inputStream = null;
        try {
            inputStream = calendarLoader.loadFrom(url);
            return serialiser.serialiseEventsFrom(inputStream);
        } catch (IOException | DateParseException e) {
            throw new CalendarFetcherException(e);
        } finally {
            closeStream(inputStream);
        }

    }

}
