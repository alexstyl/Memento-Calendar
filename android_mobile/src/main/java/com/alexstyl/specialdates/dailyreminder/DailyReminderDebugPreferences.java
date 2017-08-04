package com.alexstyl.specialdates.dailyreminder;

import android.content.Context;
import android.support.v4.util.Pair;

import com.alexstyl.specialdates.EasyPreferences;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.Months;

public final class DailyReminderDebugPreferences {

    private final EasyPreferences preferences;

    public static DailyReminderDebugPreferences newInstance(Context context) {
        return new DailyReminderDebugPreferences(EasyPreferences.createForPrivatePreferences(context, R.string.pref_dailyreminder_debug));
    }

    private DailyReminderDebugPreferences(EasyPreferences preferences) {
        this.preferences = preferences;
    }

    public Date getSelectedDate() {
        int dayOfMonth = preferences.getInt(R.string.key_debug_daily_reminder_date_fake_day, 1);
        int month = preferences.getInt(R.string.key_debug_daily_reminder_date_fake_month, Months.JANUARY);
        int year = preferences.getInt(R.string.key_debug_daily_reminder_date_fake_year, 2016);
        return Date.Companion.on(dayOfMonth, month, year);
    }

    public boolean isFakeDateEnabled() {
        return preferences.getBoolean(R.string.key_debug_daily_reminder_date_enable, false);
    }

    public void setSelectedDate(int dayOfMonth, int month, int year) {
        Pair<Integer, Integer> dayPair = new Pair<>(R.string.key_debug_daily_reminder_date_fake_day, dayOfMonth);
        Pair<Integer, Integer> monthPair = new Pair<>(R.string.key_debug_daily_reminder_date_fake_month, month);
        Pair<Integer, Integer> yearPair = new Pair<>(R.string.key_debug_daily_reminder_date_fake_year, year);
        preferences.setIntegers(dayPair, monthPair, yearPair);
    }

    public void setEnabled(boolean newValue) {
        preferences.setBoolean(R.string.key_debug_daily_reminder_date_enable, newValue);
    }
}
