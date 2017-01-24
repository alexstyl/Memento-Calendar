package com.alexstyl.specialdates.receiver;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.alexstyl.specialdates.dailyreminder.DailyReminderPreferences;
import com.alexstyl.specialdates.dailyreminder.DailyReminderScheduler;
import com.alexstyl.specialdates.wear.WearSyncService;
import com.alexstyl.specialdates.widgetprovider.TodayWidgetProvider;

public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (Intent.ACTION_BOOT_COMPLETED.equals(action)
                || Intent.ACTION_TIME_CHANGED.equals(action)) {
            startRequiredServices(context);
        }
    }

    private void startRequiredServices(Context context) {
        DailyReminderPreferences preferences = DailyReminderPreferences.newInstance(context);
        if (preferences.isEnabled()) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            DailyReminderScheduler scheduler = new DailyReminderScheduler(alarmManager, context);
            scheduler.setupReminder(preferences);
        }
        TodayWidgetProvider.updateWidgets(context);
        WearSyncService.startService(context);
    }

}
