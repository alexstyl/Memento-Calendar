package com.alexstyl.specialdates.upcoming;

import android.graphics.Typeface;
import android.view.View;

import com.alexstyl.resources.StringResources;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.DateDisplayStringCreator;
import com.alexstyl.specialdates.events.bankholidays.BankHoliday;
import com.alexstyl.specialdates.events.namedays.NamesInADate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class UpcomingRowViewModelsFactory {
    private final Date today;
    private final DateDisplayStringCreator dateDisplayCreator;
    private final ContactViewModelFactory contactViewModelFactory;
    private final StringResources stringResources;
    private final BankHolidayViewModelFactory bankHolidayViewModelFactory;
    private final NamedaysViewModelFactory namedaysViewModelFactory;

    UpcomingRowViewModelsFactory(Date today,
                                 DateDisplayStringCreator dateDisplayCreator,
                                 ContactViewModelFactory contactViewModelFactory,
                                 StringResources stringResources,
                                 BankHolidayViewModelFactory bankHolidayViewModelFactory,
                                 NamedaysViewModelFactory namedaysViewModelFactory) {
        this.today = today;
        this.dateDisplayCreator = dateDisplayCreator;
        this.contactViewModelFactory = contactViewModelFactory;
        this.stringResources = stringResources;
        this.bankHolidayViewModelFactory = bankHolidayViewModelFactory;
        this.namedaysViewModelFactory = namedaysViewModelFactory;
    }

    UpcomingEventsViewModel createViewModelFor(Date date, List<ContactEvent> contactEvents, NamesInADate namedays, BankHoliday bankHoliday) {
        String dateLabel = dateDisplayCreator.fullyFormattedDate(date);
        Typeface typeface = getTypefaceFor(date, today);

        BankHolidayViewModel bankHolidayViewModel = bankHolidayViewModelFactory.createViewModelFor(bankHoliday);
        NamedaysViewModel namedaysViewModel = namedaysViewModelFactory.createViewModelFor(date, namedays);

        ContactEventsViewModel contactEventsViewModel;
        if (contactEvents.size() > 0) {
            List<ContactEventViewModel> contactEventViewModels = new ArrayList<>();
            ContactEventViewModel viewModel = contactViewModelFactory.createViewModelFor(typeface, contactEvents.get(0));
            contactEventViewModels.add(viewModel);
            if (contactEvents.size() > 1) {
                ContactEventViewModel secondViewModel = contactViewModelFactory.createViewModelFor(typeface, contactEvents.get(1));
                contactEventViewModels.add(secondViewModel);
            }
            int remainingContactSize = contactEvents.size() > 2 ? contactEvents.size() - 2 : 0;
            String moreLabel = stringResources.getString(R.string.plus_x_more, remainingContactSize);
            contactEventsViewModel = new ContactEventsViewModel(contactEventViewModels, moreLabel, remainingContactSize == 0 ? View.GONE : View.VISIBLE);
        } else {
            contactEventsViewModel = new ContactEventsViewModel(Collections.<ContactEventViewModel>emptyList(), "", View.GONE);
        }

        return new UpcomingEventsViewModel(date, dateLabel, typeface, bankHolidayViewModel, namedaysViewModel, contactEventsViewModel);
    }

    private Typeface getTypefaceFor(Date indexDate, Date lastDate) {
        Typeface typeface;
        if (indexDate.equals(lastDate)) {
            typeface = Typeface.DEFAULT_BOLD;
        } else {
            typeface = Typeface.DEFAULT;
        }
        return typeface;
    }
}
