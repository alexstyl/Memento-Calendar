package com.alexstyl.specialdates.upcoming.view;

import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.upcoming.UpcomingEventsViewModel;

public interface OnUpcomingEventClickedListener {

    @Deprecated
    void onEventClicked(UpcomingEventsViewModel viewModel);

    @Deprecated
    void onMoreEventsClicked(UpcomingEventsViewModel viewModel);

    void onContactClicked(Contact contact);
}
