package com.alexstyl.specialdates.dailyreminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.alexstyl.android.AlarmManagerCompat;
import com.alexstyl.specialdates.TimeOfDay;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.DateAndTime;
import com.novoda.notils.logger.simple.Log;

public final class DailyReminderScheduler {

    private static final int SERVICE_REQUEST_CODE = 40;
    private final AlarmManagerCompat alarmManager;
    private final Context context;

    public DailyReminderScheduler(AlarmManagerCompat alarmManager, Context context) {
        this.alarmManager = alarmManager;
        this.context = context;
    }

    public void setupReminder(DailyReminderPreferences preferences) {
        TimeOfDay timeOfDay = preferences.getDailyReminderTimeSet();
        updateReminderTime(timeOfDay);
    }

    public void updateReminderTime(TimeOfDay timeOfDay) {
        Log.d("DAILY", "updating reminder");
        PendingIntent pendingIntent = buildPendingIntent();

        DateAndTime dateAndTime = new DateAndTime(Date.today(), timeOfDay);

        TimeOfDay now = TimeOfDay.now();
        if (now.isAfter(timeOfDay)) {
            // already passed. plan for the next day
            dateAndTime = dateAndTime.addDay(1);
        }

        alarmManager.setExact(AlarmManager.RTC, dateAndTime.toMilis(), pendingIntent);
    }

    private PendingIntent buildPendingIntent() {
        Intent intent = new Intent(context, DailyReminderIntentService.class);
        return PendingIntent.getService(context, SERVICE_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void cancelReminder() {
        alarmManager.cancel(buildPendingIntent());
    }
}
