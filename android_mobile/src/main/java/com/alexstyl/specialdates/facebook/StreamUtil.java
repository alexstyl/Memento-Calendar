package com.alexstyl.specialdates.facebook;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

final class StreamUtil {
    static InputStream streamFrom(String normalised) {
        return new ByteArrayInputStream(normalised.getBytes());
    }

    static String normaliseDate(String calendar) {
        return calendar.replaceAll("DTSTART", "DTSTART;VALUE=DATE");
    }
}
