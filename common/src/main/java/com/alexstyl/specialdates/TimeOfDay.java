package com.alexstyl.specialdates;

import static android.text.format.DateUtils.SECOND_IN_MILLIS;

public final class TimeOfDay {

    private static final long MINUTE_IN_MILLIS = SECOND_IN_MILLIS * 60;
    private static final long HOUR_IN_MILLIS = MINUTE_IN_MILLIS * 60;
    private static final String ZERO = "0";
    private static final String SEPARATOR = ":";

    private final int hour;
    private final int minute;

    public TimeOfDay(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        if (isOneDigit(hour)) {
            str.append(ZERO);
        }
        str.append(hour)
                .append(SEPARATOR);
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
        return hour;
    }

    public int getMinutes() {
        return minute;
    }

    public long toMillis() {
        return hour * HOUR_IN_MILLIS + minute * MINUTE_IN_MILLIS;
    }
}
