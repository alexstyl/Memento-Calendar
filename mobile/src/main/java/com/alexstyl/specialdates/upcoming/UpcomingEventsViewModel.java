package com.alexstyl.specialdates.upcoming;

import android.graphics.Typeface;

import com.alexstyl.specialdates.date.Date;

public final class UpcomingEventsViewModel implements UpcomingRowViewModel {

    private final Date date;
    private final String displayDateLabel;
    private final Typeface dateTypeFace;
    private final BankHolidayViewModel bankHolidayViewModel;
    private final NamedaysViewModel namedaysViewModel;
    private final ContactEventsViewModel contactEventsViewModel;

    UpcomingEventsViewModel(Date date,
                            String displayDateLabel,
                            Typeface dateTypeFace,
                            BankHolidayViewModel bankHolidayViewModel,
                            NamedaysViewModel namedaysViewModel,
                            ContactEventsViewModel contactEventsViewModel) {
        this.date = date;
        this.displayDateLabel = displayDateLabel;
        this.dateTypeFace = dateTypeFace;
        this.bankHolidayViewModel = bankHolidayViewModel;
        this.namedaysViewModel = namedaysViewModel;
        this.contactEventsViewModel = contactEventsViewModel;
    }

    @Override
    public int getViewType() {
        return UpcomingRowViewType.UPCOMING_EVENTS;
    }

    public String getDisplayDateLabel() {
        return displayDateLabel;
    }

    public Typeface getDateTypeFace() {
        return dateTypeFace;
    }

    public BankHolidayViewModel getBankHolidayViewModel() {
        return bankHolidayViewModel;
    }

    public NamedaysViewModel getNamedaysViewModel() {
        return namedaysViewModel;
    }

    public ContactEventsViewModel getContactEventsViewModel() {
        return contactEventsViewModel;
    }

    public Date getDate() {
        return date;
    }
}
