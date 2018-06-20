package com.alexstyl.specialdates;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.alexstyl.specialdates.events.peopleevents.UpcomingEventsViewRefresher;

import javax.inject.Inject;

/**
 * A {@linkplain BroadcastReceiver} that keeps track whether the user has updated some option on their device
 * external to Memento which can affect the app.
 */
public class DeviceConfigurationUpdatedReceiver extends BroadcastReceiver {

    @Inject UpcomingEventsViewRefresher viewRefresher;
    @Inject CrashAndErrorTracker tracker;

    @Override
    public void onReceive(Context context, Intent intent) {
        ((MementoApplication) context.getApplicationContext()).getApplicationModule().inject(this);

        String action = intent.getAction();
        if (Intent.ACTION_LOCALE_CHANGED.equals(action)) {
            tracker.updateLocaleUsed();
            viewRefresher.refreshViews();
        } else if (Intent.ACTION_DATE_CHANGED.equals(action)) {
            viewRefresher.refreshViews();
        }
    }

}
