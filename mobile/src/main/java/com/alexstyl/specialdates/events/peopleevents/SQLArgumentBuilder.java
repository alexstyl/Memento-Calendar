package com.alexstyl.specialdates.events.peopleevents;

import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.database.PeopleEventsContract;
import com.alexstyl.specialdates.upcoming.TimePeriod;

import java.util.Locale;

final class SQLArgumentBuilder {

    private static final String DATE = PeopleEventsContract.PeopleEvents.DATE;

    // TODO Write tests for this
    String dateBetween(TimePeriod duration) {
        return dateFrom(duration.getFrom()) + " AND " + dateTo(duration.getTo());
    }

    private static String dateFrom(Date date) {
        return String.format(Locale.US, "substr(%s,-5) >= '%s'", SQLArgumentBuilder.DATE, dateWithoutYear(date));
    }

    private static String dateTo(Date date) {
        return String.format(Locale.US, "substr(%s,-5) <= '%s'", SQLArgumentBuilder.DATE, dateWithoutYear(date));
    }

    private static String dateWithoutYear(Date date) {
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
