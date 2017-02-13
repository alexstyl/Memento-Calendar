package com.alexstyl.specialdates.date;

import com.alexstyl.specialdates.TimeOfDay;

public final class DateAndTime {
    private final Date date;
    private final TimeOfDay timeOfDay;

    public DateAndTime(Date date, TimeOfDay timeOfDay) {
        this.date = date;
        this.timeOfDay = timeOfDay;
    }

    public long toMilis() {
        return date.toMillis() + timeOfDay.toMillis();
    }

    public DateAndTime addDay(int i) {
        return new DateAndTime(date.addDay(i), timeOfDay);
    }
}
