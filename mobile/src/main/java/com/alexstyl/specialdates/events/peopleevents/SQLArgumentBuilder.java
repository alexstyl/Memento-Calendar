package com.alexstyl.specialdates.events.peopleevents;

import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.database.PeopleEventsContract;

public final class SQLArgumentBuilder {

    private static final String DATE = PeopleEventsContract.PeopleEvents.DATE;
    private static final String DATE_FROM = "substr(" + SQLArgumentBuilder.DATE + ",-5) >= ?";
    private static final String DATE_TO = "substr(" + SQLArgumentBuilder.DATE + ",-5) <= ?";
    public static final String DATE_BETWEEN_IGNORING_YEAR = DATE_FROM + " AND " + DATE_TO;

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
