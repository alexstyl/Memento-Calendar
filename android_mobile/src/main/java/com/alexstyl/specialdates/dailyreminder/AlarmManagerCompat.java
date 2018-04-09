package com.alexstyl.specialdates.dailyreminder;

import android.app.AlarmManager;
import android.app.PendingIntent;

import com.alexstyl.android.Version;

public final class AlarmManagerCompat {
    private final AlarmManager alarmManager;

    public AlarmManagerCompat(AlarmManager alarmManager) {
        this.alarmManager = alarmManager;
    }

    public void setExact(int type, long triggerAtmillis, PendingIntent operation) {
        if (Version.hasMarshmallow()) {
            alarmManager.setAndAllowWhileIdle(type, triggerAtmillis, operation);
        } else if (Version.hasKitKat()) {
            alarmManager.setExact(type, triggerAtmillis, operation);
        } else {
            alarmManager.set(AlarmManager.RTC, triggerAtmillis, operation);
        }
    }

    public void cancel(PendingIntent pendingIntent) {
        alarmManager.cancel(pendingIntent);
    }
}
