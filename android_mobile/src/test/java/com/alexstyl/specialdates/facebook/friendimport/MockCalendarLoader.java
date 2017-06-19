package com.alexstyl.specialdates.facebook.friendimport;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

class MockCalendarLoader implements CalendarLoader {

    @Override
    public InputStream loadFrom(URL url) throws IOException {
        FileInputStream stream = new FileInputStream(pathToMock());
        return new BufferedInputStream(stream);
    }

    private static String pathToMock() {
        return "android_mobile" + File.separator +
                "src" + File.separator +
                "debug" + File.separator +
                "assets" + File.separator +
                "mock" + File.separator +
                "facebook-calendar.ics";
    }
}
