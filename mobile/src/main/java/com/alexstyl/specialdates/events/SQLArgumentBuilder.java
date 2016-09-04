package com.alexstyl.specialdates.events;

import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.upcoming.LoadingTimeDuration;

import java.util.Locale;

class SQLArgumentBuilder {

    private static final String date = PeopleEventsContract.PeopleEvents.DATE;

    public String dateBetween(LoadingTimeDuration duration) {
        return dateFrom(duration.getFrom()) + " AND " + dateTo(duration.getTo());
    }

    public String dateFrom(Date date) {
        return String.format(Locale.US, "'%d' || substr(%s,-6) >= '%s'", date.getYear(), SQLArgumentBuilder.date, date.toString());
    }

    public String dateTo(Date date) {
        return String.format(Locale.US, "'%d' || substr(%s,-6) <= '%s'", date.getYear(), SQLArgumentBuilder.date, date.toString());
    }

    public String dateIn(int year) {
        return String.format(Locale.US, "'%d' || substr(date, -6) as '%s'", year, EventColumns.DATE);
    }
}
