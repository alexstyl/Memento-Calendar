package com.alexstyl.specialdates.facebook;

import com.alexstyl.specialdates.date.ContactEvent;

import java.net.URL;
import java.util.List;

interface CalendarFetcher {
    List<ContactEvent> fetchCalendarFrom(URL url) throws CalendarFetcherException;
}
