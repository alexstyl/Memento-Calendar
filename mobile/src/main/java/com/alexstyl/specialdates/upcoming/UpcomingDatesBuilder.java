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
    private static final List<ContactEvent> NO_CONTACT_EVENTS = Collections.emptyList();
    private static final DateComparator COMPARATOR = DateComparator.INSTANCE;

    private final HashMapList<AnnualDate, ContactEvent> contactEvents = new HashMapList<>();
    private final HashMap<AnnualDate, NamesInADate> namedays = new HashMap<>();
    private final HashMap<AnnualDate, BankHoliday> bankHolidays = new HashMap<>();
    private final TimePeriod duration;

    UpcomingDatesBuilder(TimePeriod duration) {
        this.duration = duration;
    }

    UpcomingDatesBuilder withContactEvents(List<ContactEvent> contactEvents) {
        for (ContactEvent contactEvent : contactEvents) {
            Date date = contactEvent.getDate();
            this.contactEvents.addValue(new AnnualDate(date), contactEvent);
        }
        return this;
    }

    UpcomingDatesBuilder withNamedays(List<NamesInADate> namedays) {
        for (NamesInADate nameday : namedays) {
            Date date = nameday.getDate();
            this.namedays.put(new AnnualDate(date), nameday);
        }
        return this;
    }

    UpcomingDatesBuilder withBankHolidays(List<BankHoliday> bankHolidays) {
        for (BankHoliday bankHoliday : bankHolidays) {
            Date date = bankHoliday.getDate();
            this.bankHolidays.put(new AnnualDate(date), bankHoliday);
        }
        return this;
    }

    public List<CelebrationDate> build() {
        if (noEventsArePresent()) {
            return NO_CELEBRATIONS;
        }

        List<CelebrationDate> celebrationDates = new ArrayList<>();
        Date indexDate = duration.getStartingDate();
        Date lastDate = duration.getEndingDate();

        while (COMPARATOR.compare(indexDate, lastDate) <= 0) {
            AnnualDate annualDate = new AnnualDate(indexDate);
            List<ContactEvent> contactEvent = getEventsOn(annualDate);
            NamesInADate namesOnDate = namedays.get(annualDate);
            BankHoliday bankHoliday = bankHolidays.get(annualDate);
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

    private List<ContactEvent> getEventsOn(AnnualDate indexDate) {
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
