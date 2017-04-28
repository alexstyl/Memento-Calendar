package com.alexstyl.specialdates.upcoming.view;

import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.upcoming.UpcomingEventsViewModel;

public interface OnUpcomingEventClickedListener {
    void onCardPressed(UpcomingEventsViewModel date);

    void onContactEventPressed(Contact contact);

    void onMoreButtonPressed(UpcomingEventsViewModel date);
}
