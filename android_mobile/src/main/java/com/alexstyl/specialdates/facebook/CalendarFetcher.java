package com.alexstyl.specialdates.facebook;

import java.net.URL;

import net.fortuna.ical4j.model.Calendar;

interface CalendarFetcher {
    Calendar fetchCalendarFrom(URL url) throws CalendarFetcherException;
}
