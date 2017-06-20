package com.alexstyl.specialdates.datedetails;

import android.content.Context;
import android.view.LayoutInflater;

import com.alexstyl.specialdates.Optional;
import com.alexstyl.specialdates.R;
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
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsObserver;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.images.UILImageLoader;
import com.alexstyl.specialdates.service.PeopleEventsProvider;
import com.alexstyl.specialdates.support.AskForSupport;
import com.alexstyl.specialdates.ui.loader.SimpleAsyncTaskLoader;

import java.util.ArrayList;
import java.util.List;

class DateDetailsLoader extends SimpleAsyncTaskLoader<DateDetailsScreenViewModel> {

    private static final int DETAILED_CARDS_NUMBER_LIMIT = 2;
    private static final int SINGLE_COLUMN = 1;

    private final Date date;
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
    private final PeopleEventsObserver peopleEventsObserver;

    DateDetailsLoader(Context context,
                      Date date,
                      AskForSupport askForSupport,
                      PeopleEventsProvider peopleEventsProvider,
                      PeopleEventsObserver peopleEventsObserver,
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
        this.peopleEventsProvider = peopleEventsProvider;
        this.peopleEventsObserver = peopleEventsObserver;
        this.namedayPreferences = namedayPreferences;
        this.bankHolidayProvider = bankHolidayProvider;
        this.supportViewModelFactory = supportViewModelFactory;
        this.peopleEventViewModelFactory = peopleEventViewModelFactory;
        this.bankHolidayViewModelFactory = bankHolidayViewModelFactory;
        this.namedayViewModelFactory = namedayViewModelFactory;
        this.namedayCalendarProvider = namedayCalendarProvider;
        this.bankHolidaysPreferences = BankHolidaysPreferences.newInstance(getContext());

        peopleEventsObserver.startObserving(new PeopleEventsObserver.OnPeopleEventsChanged() {
            @Override
            public void onPeopleEventsUpdated() {
                onContentChanged();
            }
        });
    }

    @Override
    protected void onUnregisterObserver() {
        super.onUnregisterObserver();
        peopleEventsObserver.stopObserving();
    }

    @Override
    public DateDetailsScreenViewModel loadInBackground() {
        List<DateDetailsViewModel> viewModels = new ArrayList<>();
        addSupportCardIfNeeded(viewModels);
        addBankholidaysIfEnabled(viewModels);
        addNamedaysIfEnabled(viewModels);
        addPeopleEvents(viewModels);

        int spanCount = (viewModels.size() <= DETAILED_CARDS_NUMBER_LIMIT) ? SINGLE_COLUMN : getContext().getResources().getInteger(R.integer.grid_card_columns);

        DateDetailsViewHolderFactory viewHolderFactory = createViewHolderFactory(viewModels);
        return new DateDetailsScreenViewModel(viewModels, spanCount, viewHolderFactory);
    }

    private DateDetailsViewHolderFactory createViewHolderFactory(List<DateDetailsViewModel> viewModels) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        ImageLoader imageLoader = UILImageLoader.createLoader(getContext().getResources());

        return (viewModels.size() <= 2) ?
                DateDetailsViewHolderFactory.createDetailedFactory(layoutInflater, imageLoader) :
                DateDetailsViewHolderFactory.createCompactFactory(layoutInflater, imageLoader);
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
