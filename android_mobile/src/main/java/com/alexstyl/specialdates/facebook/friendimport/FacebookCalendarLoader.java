package com.alexstyl.specialdates.facebook.friendimport;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

class FacebookCalendarLoader implements CalendarLoader {
    @Override
    public InputStream loadFrom(URL url) throws IOException {
        return url.openStream();
    }
}
