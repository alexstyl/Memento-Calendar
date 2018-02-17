package com.alexstyl.specialdates.facebook.friendimport;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

interface CalendarLoader {
    InputStream loadFrom(URL url) throws IOException;
}
