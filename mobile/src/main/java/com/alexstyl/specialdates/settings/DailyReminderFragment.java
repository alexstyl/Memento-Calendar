package com.alexstyl.specialdates.settings;

import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.analytics.Action;
import com.alexstyl.specialdates.analytics.ActionWithParameters;
import com.alexstyl.specialdates.analytics.Analytics;
import com.alexstyl.specialdates.analytics.Firebase;
import com.alexstyl.specialdates.service.DailyReminderIntentService;
import com.alexstyl.specialdates.ui.widget.TimePreference;
import com.alexstyl.specialdates.util.Utils;

import java.util.Calendar;

public class DailyReminderFragment extends MyPreferenceFragment {

    private CheckBoxPreference enablePreference;
    private Preference ringtonePreference;
    private Preference vibratePreference;
    private TimePreference timePreference;
    private Analytics analytics;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        analytics = Firebase.get(getActivity());
        setHasOptionsMenu(true);
        addPreferencesFromResource(R.xml.preference_dailyreminder);

        // the switch is controlled by the activity

        enablePreference = (CheckBoxPreference) findPreference(getString(R.string.key_daily_reminder));
        enablePreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Context context = getActivity();
                boolean isChecked = (boolean) newValue;
                MainPreferenceActivity.setDailyReminder(context, isChecked);
                ActionWithParameters event = new ActionWithParameters(Action.DAILY_REMINDER, "enabled", isChecked);
                analytics.trackAction(event);
                if (isChecked) {
                    DailyReminderIntentService.rescheduleAlarm(context);
                } else {
                    DailyReminderIntentService.cancelAlarm(context);
                }
                return true;
            }

        });

        ringtonePreference = findPreference(getString(R.string.key_daily_reminder_ringtone));
        ringtonePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                updateRingtoneSummary((String) newValue);
                return true;
            }
        });

        timePreference = (TimePreference) findPreference(getString(R.string.key_daily_reminder_time));
        timePreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                int[] time = (int[]) newValue;
                updateTimeSet(time);
                MainPreferenceActivity.setDailyReminderTime(getActivity(), time);
                DailyReminderIntentService.resetAlarm(getActivity());
                return true;
            }
        });

        vibratePreference = findPreference(getString(R.string.key_daily_reminder_vibrate_enabled));

        if (!Utils.hasVibrator(getActivity())) {
            // hide the vibrator preference if the device doesn't support
            // vibration
            getPreferenceScreen().removePreference(vibratePreference);
        }

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent up = new Intent(getActivity(), MainPreferenceActivity.class);
                up.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(up);
                getActivity().finish();
                return true;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        enablePreference.setChecked(MainPreferenceActivity.isDailyReminderSet(getActivity()));
        updateRingtoneSummary(MainPreferenceActivity.getDailyReminderRingtone(getActivity()));
        updateTimeSet(MainPreferenceActivity.getDailyReminderTime(getActivity()));
    }

    private void updateTimeSet(int[] time) {
        String timeString = getStringHour(time);
        String summary = String.format(getString(R.string.daily_reminder_time_summary), timeString);
        timePreference.setSummary(summary);

    }

    private String getStringHour(int[] time) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, time[0]);
        cal.set(Calendar.MINUTE, time[1]);
        return getHour(getActivity(), cal).toString();
    }

    // Char sequence for a 12 hour format.
    private static final CharSequence DEFAULT_FORMAT_12_HOUR = "hh:mm a";
    // Char sequence for a 24 hour format.
    private static final CharSequence DEFAULT_FORMAT_24_HOUR = "kk:mm";

    public static CharSequence getHour(Context context, Calendar cal) {
        boolean is24Hour = DateFormat.is24HourFormat(context);
        if (is24Hour) {
            return DateFormat.format(DEFAULT_FORMAT_24_HOUR, cal);
        } else {
            return DateFormat.format(DEFAULT_FORMAT_12_HOUR, cal);
        }

    }

    private void updateRingtoneSummary(String uri) {
        String name = null;
        if (!TextUtils.isEmpty(uri)) {
            Uri ringtoneUri = Uri.parse(uri);
            Ringtone ringtone = RingtoneManager.getRingtone(getActivity(), ringtoneUri);

            if (ringtone != null) {
                name = ringtone.getTitle(getActivity());
            }
        } else {
            name = getString(R.string.no_sound);
        }
        ringtonePreference.setSummary(name);
    }
}
