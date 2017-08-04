package com.alexstyl.specialdates.upcoming.widget.list;

import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.DateComparator;
import com.alexstyl.specialdates.date.TimePeriod;
import com.alexstyl.specialdates.events.bankholidays.BankHoliday;
import com.alexstyl.specialdates.events.bankholidays.BankHolidayProvider;
import com.alexstyl.specialdates.events.bankholidays.BankHolidaysPreferences;
import com.alexstyl.specialdates.events.namedays.NamedayLocale;
import com.alexstyl.specialdates.events.namedays.NamedayPreferences;
import com.alexstyl.specialdates.events.namedays.NamesInADate;
import com.alexstyl.specialdates.events.namedays.calendar.NamedayCalendar;
import com.alexstyl.specialdates.events.namedays.calendar.resource.NamedayCalendarProvider;
import com.alexstyl.specialdates.service.PeopleEventsProvider;
import com.alexstyl.specialdates.upcoming.UpcomingEventRowViewModelFactory;
import com.alexstyl.specialdates.upcoming.UpcomingEventsAdRules;
import com.alexstyl.specialdates.upcoming.UpcomingRowViewModel;
import com.alexstyl.specialdates.upcoming.UpcomingRowViewModelsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;

public class UpcomingEventsProvider {

    private static final DateComparator COMPARATOR = DateComparator.INSTANCE;

    private final PeopleEventsProvider peopleEventsProvider;
    private final NamedayPreferences namedayPreferences;
    private final BankHolidaysPreferences bankHolidaysPreferences;
    private final BankHolidayProvider bankHolidayProvider;
    private final NamedayCalendarProvider namedayCalendarProvider;
    private final UpcomingEventRowViewModelFactory upcomingRowViewModelFactory;
    private final UpcomingEventsAdRules adRules;

    public UpcomingEventsProvider(PeopleEventsProvider peopleEventsProvider,
                                  NamedayPreferences namedayPreferences,
                                  BankHolidaysPreferences bankHolidaysPreferences,
                                  BankHolidayProvider bankHolidayProvider,
                                  NamedayCalendarProvider namedayCalendarProvider,
                                  UpcomingEventRowViewModelFactory upcomingRowViewModelFactory,
                                  UpcomingEventsAdRules adRules) {
        this.peopleEventsProvider = peopleEventsProvider;
        this.namedayPreferences = namedayPreferences;
        this.bankHolidaysPreferences = bankHolidaysPreferences;
        this.bankHolidayProvider = bankHolidayProvider;
        this.namedayCalendarProvider = namedayCalendarProvider;
        this.upcomingRowViewModelFactory = upcomingRowViewModelFactory;
        this.adRules = adRules;
    }

    public List<UpcomingRowViewModel> calculateEventsBetween(TimePeriod period) {
        List<ContactEvent> contactEvents = peopleEventsProvider.getContactEventsFor(period);
        UpcomingRowViewModelsBuilder upcomingRowViewModelsBuilder = new UpcomingRowViewModelsBuilder(
                period,
                upcomingRowViewModelFactory,
                adRules
        )
                .withContactEvents(contactEvents);

        if (shouldLoadBankHolidays()) {
            List<BankHoliday> bankHolidays = bankHolidayProvider.calculateBankHolidaysBetween(period);
            upcomingRowViewModelsBuilder.withBankHolidays(bankHolidays);
        }

        if (shouldLoadNamedays()) {
            List<NamesInADate> namedays = calculateNamedaysBetween(period);
            upcomingRowViewModelsBuilder.withNamedays(namedays);
        }
        return upcomingRowViewModelsBuilder.build();
    }

    public Observable<List<UpcomingRowViewModel>> calculateEventsBetweenRX(final TimePeriod period) {
        return Observable.fromCallable(new Callable<List<UpcomingRowViewModel>>() {
            @Override
            public List<UpcomingRowViewModel> call() {
                List<ContactEvent> contactEvents = peopleEventsProvider.getContactEventsFor(period);
                UpcomingRowViewModelsBuilder upcomingRowViewModelsBuilder = new UpcomingRowViewModelsBuilder(
                        period,
                        upcomingRowViewModelFactory,
                        adRules
                )
                        .withContactEvents(contactEvents);

                if (shouldLoadBankHolidays()) {
                    List<BankHoliday> bankHolidays = bankHolidayProvider.calculateBankHolidaysBetween(period);
                    upcomingRowViewModelsBuilder.withBankHolidays(bankHolidays);
                }

                if (shouldLoadNamedays()) {
                    List<NamesInADate> namedays = calculateNamedaysBetween(period);
                    upcomingRowViewModelsBuilder.withNamedays(namedays);
                }
                return upcomingRowViewModelsBuilder.build();
            }
        });
    }

    private List<NamesInADate> calculateNamedaysBetween(TimePeriod timeDuration) {
        NamedayLocale selectedLanguage = namedayPreferences.getSelectedLanguage();
        NamedayCalendar namedayCalendar = namedayCalendarProvider.loadNamedayCalendarForLocale(selectedLanguage, timeDuration.getStartingDate().getYear());

        Date indexDate = timeDuration.getStartingDate();
        Date toDate = timeDuration.getEndingDate();
        List<NamesInADate> namedays = new ArrayList<>();

        while (COMPARATOR.compare(indexDate, toDate) < 0) {
            NamesInADate allNamedayOn = namedayCalendar.getAllNamedayOn(indexDate);
            if (allNamedayOn.nameCount() > 0) {
                namedays.add(allNamedayOn);
            }
            indexDate = indexDate.addDay(1);
        }
        return namedays;
    }

    private boolean shouldLoadBankHolidays() {
        return bankHolidaysPreferences.isEnabled();
    }

    private boolean shouldLoadNamedays() {
        return namedayPreferences.isEnabled() && !namedayPreferences.isEnabledForContactsOnly();
    }

}
