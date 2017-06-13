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
    private final List<UpcomingContactEventViewModel> contactViewModels;
    private final String moreLabel;
    private final int moreLabelVisibility;

    UpcomingEventsViewModel(Date date,
                            String displayDateLabel,
                            Typeface dateTypeFace,
                            BankHolidayViewModel bankHolidayViewModel,
                            NamedaysViewModel namedaysViewModel,
                            List<UpcomingContactEventViewModel> contactViewModels,
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

    @Override
    public long getId() {
        return date.hashCode();
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

    public List<UpcomingContactEventViewModel> getContactViewModels() {
        return contactViewModels;
    }

    public String getMoreButtonLabe() {
        return moreLabel;
    }

    @ViewVisibility
    public int getMoreButtonVisibility() {
        return moreLabelVisibility;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UpcomingEventsViewModel that = (UpcomingEventsViewModel) o;

        if (moreLabelVisibility != that.moreLabelVisibility) {
            return false;
        }
        if (!date.equals(that.date)) {
            return false;
        }
        if (!displayDateLabel.equals(that.displayDateLabel)) {
            return false;
        }
        if (!dateTypeFace.equals(that.dateTypeFace)) {
            return false;
        }
        if (!bankHolidayViewModel.equals(that.bankHolidayViewModel)) {
            return false;
        }
        if (!namedaysViewModel.equals(that.namedaysViewModel)) {
            return false;
        }
        if (!contactViewModels.equals(that.contactViewModels)) {
            return false;
        }
        return moreLabel.equals(that.moreLabel);

    }

    @Override
    public int hashCode() {
        int result = date.hashCode();
        result = 31 * result + displayDateLabel.hashCode();
        result = 31 * result + dateTypeFace.hashCode();
        result = 31 * result + bankHolidayViewModel.hashCode();
        result = 31 * result + namedaysViewModel.hashCode();
        result = 31 * result + contactViewModels.hashCode();
        result = 31 * result + moreLabel.hashCode();
        result = 31 * result + moreLabelVisibility;
        return result;
    }
}
