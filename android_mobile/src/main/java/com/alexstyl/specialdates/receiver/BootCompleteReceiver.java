package com.alexstyl.specialdates.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.alexstyl.android.AlarmManagerCompat;
import com.alexstyl.specialdates.MementoApplication;
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsViewRefresher;
import com.alexstyl.specialdates.dailyreminder.DailyReminderPreferences;
import com.alexstyl.specialdates.dailyreminder.DailyReminderScheduler;

import javax.inject.Inject;

public class BootCompleteReceiver extends BroadcastReceiver {

    @Inject PeopleEventsViewRefresher refresher;

    @Override
    public void onReceive(Context context, Intent intent) {

        ((MementoApplication) context.getApplicationContext()).getApplicationModule().inject(this);
        String action = intent.getAction();

        if (Intent.ACTION_BOOT_COMPLETED.equals(action)
                || Intent.ACTION_TIME_CHANGED.equals(action)) {
            rescheduleDailyReminder(context, DailyReminderPreferences.newInstance(context));
            refresher.refreshViews();
        }
    }

    private void rescheduleDailyReminder(Context context, DailyReminderPreferences preferences) {
        if (preferences.isEnabled()) {
            AlarmManagerCompat alarmManager = AlarmManagerCompat.from(context);
            DailyReminderScheduler scheduler = new DailyReminderScheduler(alarmManager, context.getApplicationContext());
            scheduler.setupReminder(preferences);
        }
    }

}
