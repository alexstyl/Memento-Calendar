package com.alexstyl.specialdates.datedetails;

import android.content.Context;

import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.events.bankholidays.BankHoliday;
import com.alexstyl.specialdates.events.bankholidays.BankHolidayProvider;
import com.alexstyl.specialdates.events.bankholidays.BankHolidaysPreferences;
import com.alexstyl.specialdates.events.namedays.NamedayLocale;
import com.alexstyl.specialdates.events.namedays.NamedayPreferences;
import com.alexstyl.specialdates.events.namedays.NamesInADate;
import com.alexstyl.specialdates.events.namedays.calendar.NamedayCalendar;
import com.alexstyl.specialdates.events.namedays.calendar.resource.NamedayCalendarProvider;
import com.alexstyl.specialdates.service.PeopleEventsProvider;
import com.alexstyl.specialdates.support.AskForSupport;
import com.alexstyl.specialdates.ui.loader.SimpleAsyncTaskLoader;
import com.alexstyl.specialdates.util.ContactsObserver;

import java.util.ArrayList;
import java.util.List;

class DateDetailsLoader extends SimpleAsyncTaskLoader<List<DateDetailsViewModel>> {

    private final Date date;
    private final ContactsObserver contactsObserver;
    private final AskForSupport askForSupport;
    private final BankHolidaysPreferences bankHolidaysPreferences;
    private final NamedayPreferences namedayPreferences;
    private final NamedayCalendarProvider namedayCalendarProvider;
    private final BankHolidayProvider bankHolidayProvider;
    private final PeopleEventsProvider peopleEventsProvider;
    private final SupportViewModelFactory supportViewModelFactory;
    private final BankHolidayViewModelFactory bankHolidayViewModelFactory;
    private final NamedayViewModelFactory namedayViewModelFactory;
    private final PeopleEventViewModelFactory peopleEventViewModelFactory;

    DateDetailsLoader(Context context,
                      Date date,
                      AskForSupport askForSupport,
                      PeopleEventsProvider peopleEventsProvider,
                      ContactsObserver contactsObserver,
                      NamedayPreferences namedayPreferences,
                      BankHolidayProvider bankHolidayProvider,
                      SupportViewModelFactory supportViewModelFactory,
                      PeopleEventViewModelFactory peopleEventViewModelFactory,
                      BankHolidayViewModelFactory bankHolidayViewModelFactory,
                      NamedayViewModelFactory namedayViewModelFactory,
                      NamedayCalendarProvider namedayCalendarProvider) {
        super(context);
        this.date = date;
        this.askForSupport = askForSupport;
        this.contactsObserver = contactsObserver;
        this.peopleEventsProvider = peopleEventsProvider;
        this.namedayPreferences = namedayPreferences;
        this.bankHolidayProvider = bankHolidayProvider;
        this.supportViewModelFactory = supportViewModelFactory;
        this.peopleEventViewModelFactory = peopleEventViewModelFactory;
        this.bankHolidayViewModelFactory = bankHolidayViewModelFactory;
        this.namedayViewModelFactory = namedayViewModelFactory;
        this.namedayCalendarProvider = namedayCalendarProvider;

        this.bankHolidaysPreferences = BankHolidaysPreferences.newInstance(getContext());
        contactsObserver.registerWith(
                new ContactsObserver.Callback() {
                    @Override
                    public void onContactsUpdated() {
                        onContentChanged();
                    }
                }
        );
    }

    @Override
    protected void onUnregisterObserver() {
        super.onUnregisterObserver();
        contactsObserver.unregister();
    }

    @Override
    public List<DateDetailsViewModel> loadInBackground() {
        List<DateDetailsViewModel> viewModels = new ArrayList<>();
        addSupportCardIfNeeded(viewModels);
        addBankholidaysIfEnabled(viewModels);
        addNamedaysIfEnabled(viewModels);
        addPeopleEvents(viewModels);
        return viewModels;
    }

    private void addSupportCardIfNeeded(List<DateDetailsViewModel> viewModels) {
        if (askForSupport.shouldAskForRating()) {
            viewModels.add(supportViewModelFactory.createViewModel());
        }
    }

    private void addBankholidaysIfEnabled(List<DateDetailsViewModel> viewModels) {
        if (bankHolidaysPreferences.isEnabled()) {
            Optional<BankHoliday> bankHoliday = bankHolidayProvider.calculateBankHolidayOn(date);
            if (bankHoliday.isPresent()) {
                DateDetailsViewModel viewModel = bankHolidayViewModelFactory.convertToViewModel(bankHoliday.get());
                viewModels.add(viewModel);
            }
        }
    }

    private void addNamedaysIfEnabled(List<DateDetailsViewModel> viewModels) {
        if (namedayPreferences.isEnabled()) {
            NamedayLocale locale = namedayPreferences.getSelectedLanguage();
            NamedayCalendar namedayCalendar = namedayCalendarProvider.loadNamedayCalendarForLocale(locale, date.getYear());
            NamesInADate namedays = namedayCalendar.getAllNamedayOn(date);
            viewModels.add(namedayViewModelFactory.convertToViewModels(namedays));
        }
    }

    private void addPeopleEvents(List<DateDetailsViewModel> viewModels) {
        List<ContactEvent> peopleEvents = peopleEventsProvider.getCelebrationDateOn(date);
        List<DateDetailsViewModel> dateDetailsViewModels = peopleEventViewModelFactory.convertToViewModels(peopleEvents);
        viewModels.addAll(dateDetailsViewModels);
    }
}
