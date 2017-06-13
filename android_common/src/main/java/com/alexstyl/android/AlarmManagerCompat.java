package com.alexstyl.android;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;

public final class AlarmManagerCompat {
    private final AlarmManager alarmManager;

    public static AlarmManagerCompat from(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        return new AlarmManagerCompat(alarmManager);
    }

    private AlarmManagerCompat(AlarmManager alarmManager) {
        this.alarmManager = alarmManager;
    }

    public void setExact(int type, long triggerAtmillis, PendingIntent operation) {
        if (Version.hasKitKat()) {
            alarmManager.setExact(type, triggerAtmillis, operation);
        } else {
            alarmManager.set(AlarmManager.RTC, triggerAtmillis, operation);
        }
    }

    public void cancel(PendingIntent pendingIntent) {
        alarmManager.cancel(pendingIntent);
    }
}
