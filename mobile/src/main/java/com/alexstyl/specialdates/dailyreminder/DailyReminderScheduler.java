package com.alexstyl.specialdates.dailyreminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.TimeOfDay;

public final class DailyReminderScheduler {

    private static final int SERVICE_REQUEST_CODE = 40;
    private final AlarmManager alarmManager;
    private final Context context;

    public DailyReminderScheduler(AlarmManager alarmManager, Context context) {
        this.alarmManager = alarmManager;
        this.context = context;
    }

    public void setupReminder(DailyReminderPreferences preferences) {
        TimeOfDay timeOfDay = preferences.getDailyReminderTimeSet();
        updateReminderTime(timeOfDay);
    }

    public void updateReminderTime(TimeOfDay timeOfDay) {
        PendingIntent pendingIntent = makePendingIntent();
        long triggerAtmillis = Date.today().toMillis() + timeOfDay.toMillis();

        alarmManager.setRepeating(AlarmManager.RTC, triggerAtmillis, AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private PendingIntent makePendingIntent() {
        Intent intent = new Intent(context, DailyReminderIntentService.class);
        return PendingIntent.getService(context, SERVICE_REQUEST_CODE, intent, 0);
    }

    public void cancelReminder() {
        alarmManager.cancel(makePendingIntent());
    }
}
