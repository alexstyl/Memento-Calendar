package com.alexstyl.specialdates.upcoming;

import android.graphics.Typeface;

import com.alexstyl.android.ViewVisibility;
import com.alexstyl.specialdates.date.Date;

import java.util.List;

public final class UpcomingEventsViewModel implements UpcomingRowViewModel {

    private final Date date;
    private final String displayDateLabel;
    private final Typeface dateTypeFace;
    private final BankHolidayViewModel bankHolidayViewModel;
    private final NamedaysViewModel namedaysViewModel;
    private final List<ContactEventViewModel> contactViewModels;
    private final String moreLabel;
    private final int moreLabelVisibility;

    UpcomingEventsViewModel(Date date,
                            String displayDateLabel,
                            Typeface dateTypeFace,
                            BankHolidayViewModel bankHolidayViewModel,
                            NamedaysViewModel namedaysViewModel,
                            List<ContactEventViewModel> contactViewModels,
                            String moreLabel,
                            @ViewVisibility int moreLabelVisibility
    ) {
        this.date = date;
        this.displayDateLabel = displayDateLabel;
        this.dateTypeFace = dateTypeFace;
        this.bankHolidayViewModel = bankHolidayViewModel;
        this.namedaysViewModel = namedaysViewModel;
        this.contactViewModels = contactViewModels;
        this.moreLabel = moreLabel;
        this.moreLabelVisibility = moreLabelVisibility;
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

    public Date getDate() {
        return date;
    }

    public List<ContactEventViewModel> getContactViewModels() {
        return contactViewModels;
    }

    public String getMoreButtonLabe() {
        return moreLabel;
    }

    @ViewVisibility
    public int getMoreButtonVisibility() {
        return moreLabelVisibility;
    }
}
