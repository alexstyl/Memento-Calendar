package com.alexstyl.specialdates.facebook.friendimport;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

final class StreamUtil {
    static InputStream streamFrom(String normalised) {
        return new ByteArrayInputStream(normalised.getBytes());
    }

    static String normaliseDate(String calendar) {
        return calendar.replaceAll("DTSTART", "DTSTART;VALUE=DATE");
    }

    static void closeStream(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
