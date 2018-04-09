package com.alexstyl.specialdates.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.alexstyl.specialdates.MementoApplication;
import com.alexstyl.specialdates.dailyreminder.DailyReminderScheduler;
import com.alexstyl.specialdates.dailyreminder.DailyReminderUserSettings;
import com.alexstyl.specialdates.events.peopleevents.UpcomingEventsViewRefresher;

import javax.inject.Inject;

public class BootCompleteReceiver extends BroadcastReceiver {

    @Inject UpcomingEventsViewRefresher refresher;
    @Inject DailyReminderUserSettings dailyReminderUserSettings;
    @Inject DailyReminderScheduler scheduler;

    @Override
    public void onReceive(Context context, Intent intent) {

        ((MementoApplication) context.getApplicationContext()).getApplicationModule().inject(this);
        String action = intent.getAction();

        if (Intent.ACTION_BOOT_COMPLETED.equals(action)
                || Intent.ACTION_TIME_CHANGED.equals(action)) {
            rescheduleDailyReminder(context, dailyReminderUserSettings);
            refresher.refreshViews();
        }
    }

    private void rescheduleDailyReminder(Context context, DailyReminderUserSettings userSettings) {
        if (userSettings.isEnabled()) {
            scheduler.setupReminder(userSettings);
        }
    }

}
