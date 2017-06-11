package com.alexstyl.specialdates.debug;

import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.provider.CalendarContract;
import android.widget.DatePicker;
import android.widget.Toast;

import com.alexstyl.specialdates.ExternalWidgetRefresher;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.datedetails.actions.IntentAction;
import com.alexstyl.specialdates.dailyreminder.DailyReminderDebugPreferences;
import com.alexstyl.specialdates.dailyreminder.DailyReminderIntentService;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.donate.DebugDonationPreferences;
import com.alexstyl.specialdates.events.peopleevents.DebugPeopleEventsUpdater;
import com.alexstyl.specialdates.ui.base.MementoPreferenceFragment;
import com.alexstyl.specialdates.util.AppUtils;
import com.alexstyl.specialdates.wear.WearSyncWidgetRefresher;

import java.util.Calendar;

public class DebugFragment extends MementoPreferenceFragment {

    private DailyReminderDebugPreferences dailyReminderDebugPreferences;

    @Override
    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        addPreferencesFromResource(R.xml.preference_debug);
        dailyReminderDebugPreferences = DailyReminderDebugPreferences.newInstance(getActivity());
        findPreference(R.string.key_debug_refresh_db).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                DebugPeopleEventsUpdater.newInstance(getActivity()).refresh();
                Toast.makeText(getActivity(), "Refreshing Database", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        findPreference(R.string.key_debug_refresh_widget).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                ExternalWidgetRefresher.get(getActivity()).refreshAllWidgets();
                Toast.makeText(getActivity(), "Widget(s) refreshed", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        findPreference(R.string.key_debug_daily_reminder_date_enable)
                .setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        dailyReminderDebugPreferences.setEnabled((boolean) newValue);
                        return true;
                    }
                });
        findPreference(R.string.key_debug_daily_reminder_date).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Date today = dailyReminderDebugPreferences.getSelectedDate();
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getActivity(), onDailyReminderDateSelectedListener,
                        today.getYear(), today.getMonth() - 1, today.getDayOfMonth()
                );
                datePickerDialog.show();
                return false;
            }
        });

        findPreference(R.string.key_debug_daily_reminder).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                DailyReminderIntentService.startService(getActivity());
                Toast.makeText(getActivity(), "Daily Reminder Triggered", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        findPreference(R.string.key_debug_start_calendar).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startDateIntent();
                return true;
            }
        });

        findPreference(R.string.key_debug_trigger_wear_service).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                new WearSyncWidgetRefresher(getActivity()).refreshWidget();
                return true;
            }
        });
        findPreference(R.string.key_debug_reset_donations).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                DebugDonationPreferences.newInstance(preference.getContext()).reset();
                Toast.makeText(preference.getContext(), "Donations reset. You should see ads from now on", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void startDateIntent() {
        IntentAction i = new IntentAction() {
            @Override
            public void onStartAction(Context context) throws ActivityNotFoundException {
                Calendar cal = Calendar.getInstance();
                Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
                builder.appendPath("time");
                ContentUris.appendId(builder, cal.getTimeInMillis());
                Intent intent = new Intent(Intent.ACTION_VIEW)
                        .setData(builder.build());

                startActivity(intent);

            }

            @Override
            public String getName() {
                return "date debug";
            }
        };
        AppUtils.openIntentSafely(getActivity(), i);
    }

    private final DatePickerDialog.OnDateSetListener onDailyReminderDateSelectedListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            int month1 = month + 1; // dialog picker months have 0 index
            dailyReminderDebugPreferences.setSelectedDate(dayOfMonth, month1, year);
        }
    };

}
