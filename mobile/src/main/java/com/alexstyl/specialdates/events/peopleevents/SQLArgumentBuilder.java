package com.alexstyl.specialdates.events.peopleevents;

import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.database.PeopleEventsContract;

public final class SQLArgumentBuilder {

    public static String dateWithoutYear(Date date) {
        StringBuilder stringBuilder = new StringBuilder();
        int month = date.getMonth();
        addWithLeadingZeroIfNeeded(stringBuilder, month);
        stringBuilder.append("-");
        addWithLeadingZeroIfNeeded(stringBuilder, date.getDayOfMonth());
        return stringBuilder.toString();
    }

    private static void addWithLeadingZeroIfNeeded(StringBuilder stringBuilder, int value) {
        if (value < 10) {
            stringBuilder.append("0");
        }
        stringBuilder.append(value);
    }
}
