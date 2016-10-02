package com.alexstyl.specialdates.upcoming.view;

import android.view.View;

import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.date.Date;

public interface OnUpcomingEventClickedListener {
    void onCardPressed(Date date);

    void onContactEventPressed(View view, ContactEvent contact);

}
