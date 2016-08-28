package com.alexstyl.specialdates.upcoming.ui;

import android.view.View;

import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.events.DayDate;

public interface OnUpcomingEventClickedListener {
    void onCardPressed(DayDate date);

    void onContactEventPressed(View view, ContactEvent contact);

}
