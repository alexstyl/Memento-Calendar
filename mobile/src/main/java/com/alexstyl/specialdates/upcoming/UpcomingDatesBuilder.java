package com.alexstyl.specialdates.upcoming;

import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.date.CelebrationDate;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.DateComparator;
import com.alexstyl.specialdates.date.DayDate;
import com.alexstyl.specialdates.events.bankholidays.BankHoliday;
import com.alexstyl.specialdates.events.namedays.NamesInADate;
import com.alexstyl.specialdates.events.peopleevents.ContactEvents;
import com.alexstyl.specialdates.util.HashMapList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

class UpcomingDatesBuilder {

    private static final List<ContactEvent> NO_CONTACT_EVENTS = Collections.unmodifiableList(Collections.<ContactEvent>emptyList());

    private final DateComparator comparator = DateComparator.get();
    private final HashMapList<Date, ContactEvent> contactEvents = new HashMapList<>();
    private final HashMap<Date, NamesInADate> namedays = new HashMap<>();
    private final HashMap<Date, BankHoliday> bankHolidays = new HashMap<>();

    private Optional<Date> earliestDate = Optional.absent();
    private Optional<Date> latestDate = Optional.absent();

    UpcomingDatesBuilder withContactEvents(List<ContactEvent> contactEvents) {
        for (ContactEvent contactEvent : contactEvents) {
            DayDate date = contactEvent.getDate();
            keepIfEarliestDate(date);
            keepIfLatestDate(date);
            this.contactEvents.addValue(date, contactEvent);
        }
        return this;
    }

    private void keepIfLatestDate(Date date) {
        if (latestDate.isPresent()) {
            Date previousLatestDate = latestDate.get();
            if (comparator.compare(date, previousLatestDate) > 0) {
                latestDate = new Optional<>(date);
            }
        } else {
            latestDate = new Optional<>(date);
        }
    }

    private void keepIfEarliestDate(Date date) {
        if (earliestDate.isPresent()) {
            Date previousEarliestDate = earliestDate.get();
            if (comparator.compare(date, previousEarliestDate) < 0) {
                earliestDate = new Optional<>(date);
            }
        } else {
            earliestDate = new Optional<>(date);
        }
    }

    UpcomingDatesBuilder withNamedays(List<NamesInADate> namedays) {
        for (NamesInADate nameday : namedays) {
            Date date = nameday.getDate();
            keepIfEarliestDate(date);
            keepIfLatestDate(date);
            this.namedays.put(date, nameday);
        }
        return this;
    }

    UpcomingDatesBuilder withBankHolidays(List<BankHoliday> bankHolidays) {
        for (BankHoliday bankHoliday : bankHolidays) {
            DayDate date = bankHoliday.getDate();
            keepIfEarliestDate(date);
            keepIfLatestDate(date);
            this.bankHolidays.put(date, bankHoliday);
        }
        return this;
    }

    private static final List<CelebrationDate> NO_CELEBRATIONS = Collections.emptyList();

    public List<CelebrationDate> build() {
        if (noEventsArePresent()) {
            return NO_CELEBRATIONS;
        }

        List<CelebrationDate> celebrationDates = new ArrayList<>();
        DayDate indexDate = daydateOf(earliestDate.get());
        DayDate lastDate = daydateOf(latestDate.get());
        while (comparator.compare(indexDate, lastDate) <= 0) {
            List<ContactEvent> contactEvent = contactEvents.get(indexDate);
            if (contactEvent == null) {
                contactEvent = NO_CONTACT_EVENTS;
            }
            NamesInADate namedays = this.namedays.get(indexDate);

            BankHoliday bankHoliday = bankHolidays.get(indexDate);
            if (atLeastOneEventExists(contactEvent, namedays, bankHoliday)) {
                CelebrationDate date = new CelebrationDate(
                        indexDate,
                        ContactEvents.createFrom(indexDate, contactEvent),
                        new Optional<>(namedays),
                        new Optional<>(bankHoliday)
                );
                celebrationDates.add(date);
            }
            indexDate = indexDate.addDay(1);
        }
        return celebrationDates;
    }

    private DayDate daydateOf(Date date) {
        if (date instanceof DayDate) {
            return ((DayDate) date);
        } else {
            int currentYear = DayDate.todaysYear();
            return DayDate.newInstance(date.getDayOfMonth(), date.getMonth(), currentYear);
        }
    }

    private boolean noEventsArePresent() {
        return !earliestDate.isPresent() || !latestDate.isPresent();
    }

    private boolean atLeastOneEventExists(List<ContactEvent> contactEvent, NamesInADate namedays, BankHoliday bankHoliday) {
        return contactEvent.size() > 0 || namedays != null || bankHoliday != null;
    }
}
