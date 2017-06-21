package com.alexstyl.specialdates.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.alexstyl.android.AlarmManagerCompat;
import com.alexstyl.specialdates.PeopleEventsViewRefresher;
import com.alexstyl.specialdates.dailyreminder.DailyReminderPreferences;
import com.alexstyl.specialdates.dailyreminder.DailyReminderScheduler;

public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (Intent.ACTION_BOOT_COMPLETED.equals(action)
                || Intent.ACTION_TIME_CHANGED.equals(action)) {
            rescheduleDailyReminder(context, DailyReminderPreferences.newInstance(context));
            PeopleEventsViewRefresher.get(context).updateAllViews();
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
