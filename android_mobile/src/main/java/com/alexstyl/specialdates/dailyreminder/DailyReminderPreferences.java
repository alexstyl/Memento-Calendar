package com.alexstyl.specialdates.dailyreminder;

import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;

import com.alexstyl.specialdates.EasyPreferences;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.TimeOfDay;

public final class DailyReminderPreferences {

    private static final String DEFAULT_DAILY_REMINDER_TIME = "08:00";
    private final EasyPreferences preferences;

    public static DailyReminderPreferences newInstance(Context context) {
        EasyPreferences defaultPreferences = EasyPreferences.createForDefaultPreferences(context);
        return new DailyReminderPreferences(defaultPreferences);
    }

    private DailyReminderPreferences(EasyPreferences preferences) {
        this.preferences = preferences;
    }

    public void setEnabled(boolean enabled) {
        preferences.setBoolean(R.string.key_daily_reminder, enabled);
    }

    public void setDailyReminderTime(TimeOfDay time) {
        preferences.setString(R.string.key_daily_reminder_time, time.toString());
    }

    public TimeOfDay getDailyReminderTimeSet() {
        String[] time = preferences.getString(R.string.key_daily_reminder_time, DEFAULT_DAILY_REMINDER_TIME)
                .split(":");
        return new TimeOfDay(
                Integer.parseInt(time[0]),
                Integer.parseInt(time[1])
        );
    }

    public boolean isEnabled() {
        return preferences.getBoolean(R.string.key_daily_reminder, true);
    }

    public Uri getRingtoneSelected() {
        Uri defaultUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        String rawRingtone = preferences.getString(R.string.key_daily_reminder_ringtone, defaultUri.toString());
        return Uri.parse(rawRingtone);
    }

    public boolean getVibrationSet() {
        return preferences.getBoolean(R.string.key_daily_reminder_vibrate_enabled, false);
    }

}
