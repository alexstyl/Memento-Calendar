package com.alexstyl.specialdates.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.service.DailyReminderIntentService;
import com.alexstyl.specialdates.theming.Themer;
import com.alexstyl.specialdates.ui.activity.MainActivity;
import com.alexstyl.specialdates.ui.base.MementoPreferenceActivity;
import com.alexstyl.specialdates.ui.widget.MementoToolbar;
import com.novoda.notils.caster.Views;

public class MainPreferenceActivity extends MementoPreferenceActivity {

    private static final String DEFAULT_DAILY_REMINDER_TIME = "08:00";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Themer.get().initialiseActivity(this);
        setContentView(R.layout.activity_settings);

        MementoToolbar toolbar = Views.findById(this, R.id.memento_toolbar);
        setSupportActionBar(toolbar);
        toolbar.displayAsUp();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent up = new Intent(this, MainActivity.class);
                up.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(up);
                finish();
                return true;

            default:
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Returns the user selected hour of the day to run the
     * {@link DailyReminderIntentService}
     *
     * @param context The context to use
     */
    public static int[] getDailyReminderTime(Context context) {
        int[] hour = new int[2];
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
        String[] time = preference.getString(
                context.getString(R.string.key_daily_reminder_time),
                DEFAULT_DAILY_REMINDER_TIME
        ).split(":");
        hour[0] = Integer.valueOf(time[0]);
        hour[1] = Integer.valueOf(time[1]);

        return hour;
    }

    /**
     * Sets the time for the daily reminder to fire
     *
     * @param context The context to use
     * @param time    The time to set
     * @return
     */
    public static boolean setDailyReminderTime(Context context, int[] time) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(context.getString(R.string.key_daily_reminder_time), time[0] + ":" + time[1]);
        return editor.commit();

    }

    /**
     * Returns true if the user has selected to be notified about daily events
     *
     * @param context The context to use
     */
    public static boolean isDailyReminderSet(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(context.getString(R.string.key_daily_reminder), true);
    }

    public static boolean setDailyReminder(Context context, boolean value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(context.getString(R.string.key_daily_reminder), value);
        return editor.commit();
    }

    /**
     * Returns the ringtone set for the Daily Reminder notification. Returns the
     * device default notification sound if no ringtone has been set yet
     *
     * @param context The context to use
     */
    public static String getDailyReminderRingtone(Context context) {
        Uri defaultUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        String key = context.getString(R.string.key_daily_reminder_ringtone);
        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
        String selectedUri = preference.getString(key, null);
        if (selectedUri == null) {
            Editor editor = preference.edit();
            String defaultUriStr = defaultUri.toString();
            editor.putString(key, defaultUriStr);
            editor.apply();
            selectedUri = defaultUriStr;
        }

        return selectedUri;
    }

    /**
     * Returns whether the user has selected
     *
     * @param context The context to use
     */
    public static boolean getDailyReminderVibrationSet(Context context) {

        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);
        return preference.getBoolean(
                context.getString(R.string.key_daily_reminder_vibrate_enabled), false
        );
    }
}
