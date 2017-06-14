package com.alexstyl.specialdates.facebook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;

import static com.alexstyl.specialdates.facebook.StreamUtil.normaliseDate;
import static com.alexstyl.specialdates.facebook.StreamUtil.streamFrom;

class FacebookCalendarFetcher implements CalendarFetcher {

    @Override
    public Calendar fetchCalendarFrom(URL url) throws CalendarFetcherException {
        try {
            InputStream originalStream = url.openStream();
            String normalisedString = normaliseDate(convertToString(originalStream));
            originalStream.close();

            InputStream normalisedStream = streamFrom(normalisedString);

            CalendarBuilder builder = new CalendarBuilder();
            return builder.build(normalisedStream);
        } catch (IOException | ParserException e) {
            throw new CalendarFetcherException("Unable to fetch calendar: " + e);
        }
    }

    private String convertToString(InputStream originalStream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = originalStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString("UTF-8");
    }
}
