package com.alexstyl.specialdates.upcoming;

import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.date.CelebrationDate;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.DateComparator;
import com.alexstyl.specialdates.events.bankholidays.BankHoliday;
import com.alexstyl.specialdates.events.namedays.NamesInADate;
import com.alexstyl.specialdates.events.peopleevents.ContactEvents;
import com.alexstyl.specialdates.util.HashMapList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

final class UpcomingDatesBuilder {

    private static final List<CelebrationDate> NO_CELEBRATIONS = Collections.emptyList();
    private static final List<ContactEvent> NO_CONTACT_EVENTS = Collections.unmodifiableList(Collections.<ContactEvent>emptyList());
    private static final DateComparator COMPARATOR = DateComparator.INSTANCE;

    private final HashMapList<Date, ContactEvent> contactEvents = new HashMapList<>();
    private final HashMap<Date, NamesInADate> namedays = new HashMap<>();
    private final HashMap<Date, BankHoliday> bankHolidays = new HashMap<>();
    private final LoadingTimeDuration duration;

    UpcomingDatesBuilder(LoadingTimeDuration duration) {
        this.duration = duration;
    }

    UpcomingDatesBuilder withContactEvents(List<ContactEvent> contactEvents) {
        for (ContactEvent contactEvent : contactEvents) {
            Date date = contactEvent.getDate();
            this.contactEvents.addValue(date, contactEvent);
        }
        return this;
    }

    UpcomingDatesBuilder withNamedays(List<NamesInADate> namedays) {
        for (NamesInADate nameday : namedays) {
            Date date = nameday.getDate();
            this.namedays.put(date, nameday);
        }
        return this;
    }

    UpcomingDatesBuilder withBankHolidays(List<BankHoliday> bankHolidays) {
        for (BankHoliday bankHoliday : bankHolidays) {
            Date date = bankHoliday.getDate();
            this.bankHolidays.put(date, bankHoliday);
        }
        return this;
    }

    public List<CelebrationDate> build() {
        if (noEventsArePresent()) {
            return NO_CELEBRATIONS;
        }

        List<CelebrationDate> celebrationDates = new ArrayList<>();
        Date indexDate = duration.getFrom();
        Date lastDate = duration.getTo();

        while (COMPARATOR.compare(indexDate, lastDate) <= 0) {
            List<ContactEvent> contactEvent = getEventsOn(indexDate);
            NamesInADate namesOnDate = namedays.get(indexDate);
            BankHoliday bankHoliday = bankHolidays.get(indexDate);
            if (atLeastOneEventExists(contactEvent, namesOnDate, bankHoliday)) {
                CelebrationDate date = new CelebrationDate(
                        indexDate,
                        ContactEvents.createFrom(indexDate, contactEvent),
                        new Optional<>(namesOnDate),
                        new Optional<>(bankHoliday)
                );
                celebrationDates.add(date);
            }
            indexDate = indexDate.addDay(1);
        }
        return celebrationDates;
    }

    private List<ContactEvent> getEventsOn(Date indexDate) {
        List<ContactEvent> contactEvent = contactEvents.get(indexDate);
        if (contactEvent == null) {
            return NO_CONTACT_EVENTS;
        } else {
            return contactEvent;
        }
    }

    private boolean noEventsArePresent() {
        return contactEvents.isEmpty() && namedays.isEmpty() && bankHolidays.isEmpty();
    }

    private boolean atLeastOneEventExists(List<ContactEvent> contactEvent, NamesInADate namedays, BankHoliday bankHoliday) {
        return contactEvent.size() > 0 || namedays != null || bankHoliday != null;
    }
}
