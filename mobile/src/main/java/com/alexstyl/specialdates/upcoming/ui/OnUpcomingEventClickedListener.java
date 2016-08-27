package com.alexstyl.specialdates.upcoming.ui;

import android.view.View;

import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.events.DayDate;
import com.novoda.notils.logger.simple.Log;

public interface OnUpcomingEventClickedListener {
    void onCardPressed(DayDate date);

    void onContactEventPressed(View view, ContactEvent contact);

    OnUpcomingEventClickedListener NO_OP = new OnUpcomingEventClickedListener() {
        @Override
        public void onCardPressed(DayDate date) {
            Log.w("onCardPressed with no listener");
        }

        @Override
        public void onContactEventPressed(View view, ContactEvent contact) {
            Log.w("onContactEventPressed with no listener");
        }
    };
}
