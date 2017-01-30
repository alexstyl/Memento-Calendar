package com.alexstyl.specialdates.upcoming;

import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.DateComparator;
import com.alexstyl.specialdates.date.TimePeriod;
import com.alexstyl.specialdates.events.bankholidays.BankHoliday;
import com.alexstyl.specialdates.events.namedays.NamesInADate;
import com.alexstyl.specialdates.util.HashMapList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

final class UpcomingRowViewModelsBuilder {

    private static final List<UpcomingRowViewModel> NO_CELEBRATIONS = Collections.emptyList();
    private static final List<ContactEvent> NO_CONTACT_EVENTS = Collections.emptyList();
    private static final DateComparator COMPARATOR = DateComparator.INSTANCE;

    private final HashMapList<AnnualDate, ContactEvent> contactEvents = new HashMapList<>();
    private final HashMap<AnnualDate, NamesInADate> namedays = new HashMap<>();
    private final HashMap<AnnualDate, BankHoliday> bankHolidays = new HashMap<>();
    private final TimePeriod duration;
    private final UpcomingRowViewModelsFactory upcomingRowViewModelFactory;

    UpcomingRowViewModelsBuilder(TimePeriod duration, UpcomingRowViewModelsFactory upcomingRowViewModelFactory) {
        this.duration = duration;
        this.upcomingRowViewModelFactory = upcomingRowViewModelFactory;
    }

    UpcomingRowViewModelsBuilder withContactEvents(List<ContactEvent> contactEvents) {
        for (ContactEvent contactEvent : contactEvents) {
            Date date = contactEvent.getDate();
            this.contactEvents.addValue(new AnnualDate(date), contactEvent);
        }
        return this;
    }

    UpcomingRowViewModelsBuilder withNamedays(List<NamesInADate> namedays) {
        for (NamesInADate nameday : namedays) {
            Date date = nameday.getDate();
            this.namedays.put(new AnnualDate(date), nameday);
        }
        return this;
    }

    UpcomingRowViewModelsBuilder withBankHolidays(List<BankHoliday> bankHolidays) {
        for (BankHoliday bankHoliday : bankHolidays) {
            Date date = bankHoliday.getDate();
            this.bankHolidays.put(new AnnualDate(date), bankHoliday);
        }
        return this;
    }

    public List<UpcomingRowViewModel> build() {
        if (noEventsArePresent()) {
            return NO_CELEBRATIONS;
        }

        List<UpcomingRowViewModel> celebrationDates = new ArrayList<>();
        Date indexDate = duration.getStartingDate();
        Date lastDate = duration.getEndingDate();

        while (COMPARATOR.compare(indexDate, lastDate) <= 0) {
            AnnualDate annualDate = new AnnualDate(indexDate);

            if (containsAnyEventsOn(annualDate)) {
                UpcomingEventsViewModel viewModel = upcomingRowViewModelFactory.createViewModelFor(
                        indexDate,
                        getEventsOn(annualDate),
                        this.namedays.get(annualDate),
                        bankHolidays.get(annualDate)
                );
                celebrationDates.add(viewModel);
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

    private boolean containsAnyEventsOn(AnnualDate date) {
        return getEventsOn(date).size() > 0 || namedays.containsKey(date) || bankHolidays.containsKey(date);
    }
}
