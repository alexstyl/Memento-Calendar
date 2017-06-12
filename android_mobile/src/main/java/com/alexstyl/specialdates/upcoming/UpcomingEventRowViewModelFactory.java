package com.alexstyl.specialdates.upcoming;

import android.graphics.Typeface;
import android.view.View;

import com.alexstyl.resources.StringResources;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.MonthInt;
import com.alexstyl.specialdates.events.bankholidays.BankHoliday;
import com.alexstyl.specialdates.events.namedays.NamesInADate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class UpcomingEventRowViewModelFactory {
    private final Date today;
    private final UpcomingDateStringCreator dateCreator;
    private final ContactViewModelFactory contactViewModelFactory;
    private final StringResources stringResources;
    private final BankHolidayViewModelFactory bankHolidayViewModelFactory;
    private final NamedaysViewModelFactory namedaysViewModelFactory;
    private final MonthLabels monthLabels;

    public UpcomingEventRowViewModelFactory(Date today,
                                            UpcomingDateStringCreator dateCreator,
                                            ContactViewModelFactory contactViewModelFactory,
                                            StringResources stringResources,
                                            BankHolidayViewModelFactory bankHolidayViewModelFactory,
                                            NamedaysViewModelFactory namedaysViewModelFactory,
                                            MonthLabels monthLabels) {
        this.today = today;
        this.dateCreator = dateCreator;
        this.contactViewModelFactory = contactViewModelFactory;
        this.stringResources = stringResources;
        this.bankHolidayViewModelFactory = bankHolidayViewModelFactory;
        this.namedaysViewModelFactory = namedaysViewModelFactory;
        this.monthLabels = monthLabels;
    }

    UpcomingRowViewModel createEventViewModel(Date date, List<ContactEvent> contactEvents, NamesInADate namedays, BankHoliday bankHoliday) {
        String dateLabel = dateCreator.createLabelFor(date);
        Typeface typeface = getTypefaceFor(date, today);

        BankHolidayViewModel bankHolidayViewModel = bankHolidayViewModelFactory.createViewModelFor(bankHoliday);
        NamedaysViewModel namedaysViewModel = namedaysViewModelFactory.createViewModelFor(date, namedays);

        if (contactEvents.size() > 0) {
            List<UpcomingContactEventViewModel> upcomingContactEventViewModels = new ArrayList<>();
            UpcomingContactEventViewModel viewModel = contactViewModelFactory.createViewModelFor(typeface, date, contactEvents.get(0));
            upcomingContactEventViewModels.add(viewModel);
            if (contactEvents.size() > 1) {
                UpcomingContactEventViewModel secondViewModel = contactViewModelFactory.createViewModelFor(typeface, date, contactEvents.get(1));
                upcomingContactEventViewModels.add(secondViewModel);
            }
            int remainingContactSize = contactEvents.size() > 2 ? contactEvents.size() - 2 : 0;
            String moreLabel = stringResources.getString(R.string.plus_x_more, remainingContactSize);
            return new UpcomingEventsViewModel(
                    date,
                    dateLabel,
                    typeface,
                    bankHolidayViewModel,
                    namedaysViewModel,
                    upcomingContactEventViewModels,
                    moreLabel,
                    remainingContactSize == 0 ? View.GONE : View.VISIBLE
            );
        } else {
            return new UpcomingEventsViewModel(
                    date,
                    dateLabel,
                    typeface,
                    bankHolidayViewModel,
                    namedaysViewModel,
                    Collections.<UpcomingContactEventViewModel>emptyList(),
                    "",
                    View.GONE
            );
        }

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

    UpcomingRowViewModel createRowForMonth(@MonthInt int month) {
        return new MonthHeaderViewModel(monthLabels.getMonthOfYear(month));
    }

    UpcomingRowViewModel createRowForYear(int year) {
        return new YearHeaderViewModel(String.valueOf(year));
    }
}
