package com.alexstyl.specialdates.facebook.friendimport;

class CalendarFetcherException extends Throwable {
    CalendarFetcherException(String message) {
        super(message);
    }

    CalendarFetcherException(Exception e) {
        super(e);
    }
}
