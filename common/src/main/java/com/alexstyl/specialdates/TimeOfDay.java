package com.alexstyl.specialdates;

import org.joda.time.LocalTime;

import static android.text.format.DateUtils.SECOND_IN_MILLIS;

public final class TimeOfDay {

    private static final long MINUTE_IN_MILLIS = SECOND_IN_MILLIS * 60;
    private static final long HOUR_IN_MILLIS = MINUTE_IN_MILLIS * 60;
    private static final String ZERO = "0";
    private static final String SEPARATOR = ":";
    private LocalTime dateTime;

    public static TimeOfDay now() {
        return new TimeOfDay(LocalTime.now());
    }

    private TimeOfDay(LocalTime dateTime) {
        this.dateTime = dateTime;
    }

    public TimeOfDay(int hour, int minute) {
        this(new LocalTime(hour, minute));
    }

    @Override
    public String toString() {
        int hour = getHours();
        StringBuilder str = new StringBuilder();
        if (isOneDigit(hour)) {
            str.append(ZERO);
        }
        str.append(hour)
                .append(SEPARATOR);
        int minute = getMinutes();
        if (isOneDigit(minute)) {
            str.append(ZERO);
        }
        str.append(minute);
        return str.toString();
    }

    private boolean isOneDigit(int value) {
        return value < 10;
    }

    public int getHours() {
        return dateTime.getHourOfDay();
    }

    public int getMinutes() {
        return dateTime.getMinuteOfHour();
    }

    public long toMillis() {
        return dateTime.getMillisOfDay();
    }

    public boolean isAfter(TimeOfDay timeOfDay) {
        return dateTime.isAfter(timeOfDay.dateTime);
    }
}
