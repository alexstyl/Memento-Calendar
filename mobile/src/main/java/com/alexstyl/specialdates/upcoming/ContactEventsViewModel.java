package com.alexstyl.specialdates.upcoming;

import com.alexstyl.android.ViewVisibility;

import java.util.List;

public final class ContactEventsViewModel {
    private final String moreButtonLabe;
    @ViewVisibility
    private final int moreButtonVisibility;
    private final List<ContactEventViewModel> contactViewModels;

    ContactEventsViewModel(List<ContactEventViewModel> contactViewModels,
                           String moreButtonLabel,
                           @ViewVisibility int moreButtonVisibility) {
        this.moreButtonLabe = moreButtonLabel;
        this.moreButtonVisibility = moreButtonVisibility;
        this.contactViewModels = contactViewModels;
    }

    public String getMoreButtonLabe() {
        return moreButtonLabe;
    }

    @ViewVisibility
    public int getMoreButtonVisibility() {
        return moreButtonVisibility;
    }

    public List<ContactEventViewModel> getContactViewModels() {
        return contactViewModels;
    }
}
