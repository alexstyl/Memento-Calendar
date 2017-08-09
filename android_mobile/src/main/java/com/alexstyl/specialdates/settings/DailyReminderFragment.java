package com.alexstyl.specialdates.settings;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.support.annotation.NonNull;
import android.text.format.DateFormat;

import com.alexstyl.android.AlarmManagerCompat;
import com.alexstyl.android.preferences.widget.TimePreference;
import com.alexstyl.specialdates.AppComponent;
import com.alexstyl.specialdates.MementoApplication;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.TimeOfDay;
import com.alexstyl.specialdates.analytics.Analytics;
import com.alexstyl.specialdates.dailyreminder.DailyReminderPreferences;
import com.alexstyl.specialdates.dailyreminder.DailyReminderScheduler;
import com.alexstyl.specialdates.permissions.PermissionChecker;
import com.alexstyl.specialdates.ui.base.MementoPreferenceFragment;

import javax.inject.Inject;
import java.util.Calendar;

public class DailyReminderFragment extends MementoPreferenceFragment {

    private static final int EXTERNAL_STORAGE_REQUEST_CODE = 15;

    private CheckBoxPreference enablePreference;
    private RingtonePreference ringtonePreference;
    private TimePreference timePreference;
    private PermissionChecker permissionChecker;
    private DailyReminderScheduler scheduler;
    private DailyReminderPreferences preferences;
    @Inject Analytics analytics;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = getActivity().getApplicationContext();
        scheduler = new DailyReminderScheduler(AlarmManagerCompat.from(context), context);
        AppComponent applicationModule = ((MementoApplication) getActivity().getApplication()).getApplicationModule();
        applicationModule.inject(this);
        addPreferencesFromResource(R.xml.preference_dailyreminder);
        permissionChecker = new PermissionChecker(getActivity());
        enablePreference = findPreference(R.string.key_daily_reminder);

        preferences = DailyReminderPreferences.newInstance(getActivity());
        enablePreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean isEnabled = (boolean) newValue;
                preferences.setEnabled(isEnabled);

                if (isEnabled) {
                    analytics.trackDailyReminderEnabled();
                    scheduler.setupReminder(preferences);
                } else {
                    analytics.trackDailyReminderDisabled();
                    scheduler.cancelReminder();
                }
                return true;
            }

        });

        ringtonePreference = findPreference(R.string.key_daily_reminder_ringtone);
        ringtonePreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (permissionChecker.canReadExternalStorage()) {
                    // the permission exists. Let the system handle the event
                    return false;
                } else {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_REQUEST_CODE);
                    return true;
                }
            }

        });
        ringtonePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                updateRingtoneSummaryFor(Uri.parse((String) newValue));
                return true;
            }
        });

        timePreference = findPreference(R.string.key_daily_reminder_time);
        timePreference.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                int[] time = (int[]) newValue;
                TimeOfDay timeOfDay = new TimeOfDay(time[0], time[1]);
                updateTimeSet(timeOfDay);
                analytics.trackDailyReminderTimeUpdated(timeOfDay);
                preferences.setDailyReminderTime(timeOfDay);
                scheduler.updateReminderTime(timeOfDay);
                return true;
            }
        });

        hideVibratorSettingIfNotPresent();
    }

    private void hideVibratorSettingIfNotPresent() {
        if (hasNoVibratorHardware()) {
            Preference vibratePreference = findPreference(getString(R.string.key_daily_reminder_vibrate_enabled));
            getPreferenceScreen().removePreference(vibratePreference);
        }
    }

    private boolean hasNoVibratorHardware() {
        Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        return !vibrator.hasVibrator();
    }

    @Override
    public void onResume() {
        super.onResume();
        enablePreference.setChecked(preferences.isEnabled());
        updateRingtoneSummaryFor(preferences.getRingtoneSelected());

        TimeOfDay timeOfDay = preferences.getDailyReminderTimeSet();
        updateTimeSet(timeOfDay);
    }

    private void updateTimeSet(TimeOfDay time) {
        String timeString = getStringHour(time);
        String summary = String.format(getString(R.string.daily_reminder_time_summary), timeString);
        timePreference.setSummary(summary);

    }

    private String getStringHour(TimeOfDay time) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, time.getHours());
        cal.set(Calendar.MINUTE, time.getMinutes());
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == EXTERNAL_STORAGE_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            ringtonePreference.onClick();
        }
    }

    private void updateRingtoneSummaryFor(Uri ringtoneUri) {
        String name = null;
        if (ringtoneUri.toString().length() > 0) {
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
