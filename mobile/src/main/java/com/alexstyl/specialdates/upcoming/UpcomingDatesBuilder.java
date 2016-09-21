package com.alexstyl.specialdates.upcoming;

import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.events.bankholidays.BankHoliday;
import com.alexstyl.specialdates.date.CelebrationDate;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.events.peopleevents.ContactEvents;
import com.alexstyl.specialdates.date.DayDate;
import com.alexstyl.specialdates.events.namedays.NamesInADate;
import com.alexstyl.specialdates.util.HashMapList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class UpcomingDatesBuilder {

    private static final List<ContactEvent> NO_CONTACT_EVENTS = Collections.unmodifiableList(new ArrayList<ContactEvent>());

    private final HashMapList<DayDate, ContactEvent> contactEvents = new HashMapList<>();
    private final HashMap<DayDate, NamesInADate> namedays = new HashMap<>();
    private final HashMap<DayDate, BankHoliday> bankHolidays = new HashMap<>();

    private Optional<DayDate> earliestDate = Optional.absent();
    private Optional<DayDate> latestDate = Optional.absent();

    public UpcomingDatesBuilder withContactEvents(List<ContactEvent> contactEvents) {
        for (ContactEvent contactEvent : contactEvents) {
            DayDate date = contactEvent.getDate();
            keepIfEarliestDate(date);
            keepIfLatestDate(date);
            this.contactEvents.addValue(date, contactEvent);
        }
        return this;
    }

    private void keepIfLatestDate(DayDate date) {
        if (latestDate.isPresent()) {
            DayDate previousLatestDate = latestDate.get();
            if (date.isAfter(previousLatestDate)) {
                latestDate = new Optional<>(date);
            }
        } else {
            latestDate = new Optional<>(date);
        }
    }

    private void keepIfEarliestDate(DayDate date) {
        if (earliestDate.isPresent()) {
            DayDate previousEarliestDate = earliestDate.get();
            if (date.isBefore(previousEarliestDate)) {
                earliestDate = new Optional<>(date);
            }
        } else {
            earliestDate = new Optional<>(date);
        }
    }

    public UpcomingDatesBuilder withNamedays(List<NamesInADate> namedays) {
//        for (NamesInADate nameday : namedays) {
//            DayDate date = nameday.getDate();
//            keepIfEarliestDate(date);
//            keepIfLatestDate(date);
//            this.namedays.put(date, nameday);
//        }
        // TODO
        throw new UnsupportedOperationException("");
//        return this;
    }

    public UpcomingDatesBuilder withBankHolidays(List<BankHoliday> bankHolidays) {
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
        DayDate indexDate = earliestDate.get();
        DayDate lastDate = latestDate.get();
        while (indexDate.compareTo(lastDate) <= 0) {
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

    private boolean noEventsArePresent() {
        return !earliestDate.isPresent() || !latestDate.isPresent();
    }

    private boolean atLeastOneEventExists(List<ContactEvent> contactEvent, NamesInADate namedays, BankHoliday bankHoliday) {
        return contactEvent.size() > 0 || namedays != null || bankHoliday != null;
    }
}
