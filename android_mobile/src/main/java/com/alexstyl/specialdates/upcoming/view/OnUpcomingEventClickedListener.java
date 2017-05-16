package com.alexstyl.specialdates.upcoming.view;

import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.upcoming.UpcomingEventsViewModel;

public interface OnUpcomingEventClickedListener {
    void onEventClicked(UpcomingEventsViewModel viewModel);

    void onContactClicked(Contact contact);

    void onMoreEventsClicked(UpcomingEventsViewModel viewModel);
}
