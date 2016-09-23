package com.alexstyl.specialdates.events.peopleevents;

import com.alexstyl.specialdates.date.DayDate;
import com.alexstyl.specialdates.events.database.EventsDBContract.AnnualEventsContract;
import com.alexstyl.specialdates.events.database.PeopleEventsContract;
import com.alexstyl.specialdates.upcoming.LoadingTimeDuration;

import java.util.Locale;

class SQLArgumentBuilder {

    private static final String date = PeopleEventsContract.PeopleEvents.DATE;

    String dateIn(int year) {
        return String.format(Locale.US, "'%d' || substr(date, -6) as '%s'", year, AnnualEventsContract.DATE);
    }

    String dateBetween(LoadingTimeDuration duration) {
        return dateFrom(duration.getFrom()) + " AND " + dateTo(duration.getTo());
    }

    private String dateFrom(DayDate date) {
        return String.format(Locale.US, "'%d' || substr(%s,-6) >= '%s'", date.getYear(), SQLArgumentBuilder.date, date.toString());
    }

    private String dateTo(DayDate date) {
        return String.format(Locale.US, "'%d' || substr(%s,-6) <= '%s'", date.getYear(), SQLArgumentBuilder.date, date.toString());
    }
}
