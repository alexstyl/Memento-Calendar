package com.alexstyl.specialdates.debug;

import android.app.DatePickerDialog;
import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.provider.CalendarContract;
import android.widget.DatePicker;
import android.widget.Toast;

import com.alexstyl.specialdates.DebugAppComponent;
import com.alexstyl.specialdates.DebugApplication;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.contact.ContactsProvider;
import com.alexstyl.specialdates.dailyreminder.DailyReminderDebugPreferences;
import com.alexstyl.specialdates.dailyreminder.DailyReminderIntentService;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.donate.DebugDonationPreferences;
import com.alexstyl.specialdates.events.namedays.NamedayUserSettings;
import com.alexstyl.specialdates.events.peopleevents.DebugPeopleEventsUpdater;
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsViewRefresher;
import com.alexstyl.specialdates.facebook.friendimport.FacebookFriendsIntentService;
import com.alexstyl.specialdates.facebook.login.FacebookLogInActivity;
import com.alexstyl.specialdates.support.AskForSupport;
import com.alexstyl.specialdates.ui.base.MementoPreferenceFragment;
import com.alexstyl.specialdates.wear.WearSyncPeopleEventsView;

import javax.inject.Inject;
import java.util.Calendar;

public class DebugFragment extends MementoPreferenceFragment {

    private DailyReminderDebugPreferences dailyReminderDebugPreferences;
    @Inject NamedayUserSettings namedayUserSettings;
    @Inject ContactsProvider contactsProvider;
    @Inject PeopleEventsViewRefresher refresher;

    @Override
    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);

        DebugAppComponent debugAppComponent = ((DebugApplication) getActivity().getApplication()).getDebugAppComponent();
        debugAppComponent.inject(this);

        addPreferencesFromResource(R.xml.preference_debug);
        dailyReminderDebugPreferences = DailyReminderDebugPreferences.newInstance(getActivity());
        findPreference(R.string.key_debug_refresh_db).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                DebugPeopleEventsUpdater.newInstance(getActivity(), namedayUserSettings, contactsProvider).refresh();
                showToast("Refreshing Database");
                return true;
            }
        });
        findPreference(R.string.key_debug_refresh_widget).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {
                refresher.updateAllViews();
                showToast("Widget(s) refreshed");
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
                showToast("Daily Reminder Triggered");
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
                new WearSyncPeopleEventsView(getActivity()).requestUpdate();
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
        findPreference(R.string.key_debug_trigger_support).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                DebugPreferences.newInstance(preference.getContext(), R.string.pref_call_to_rate).wipe();
                new AskForSupport(preference.getContext()).requestForRatingSooner();
                String message = "Support triggered. You should now see a prompt to rate the app when you launch it";
                showToast(message);
                return true;
            }
        });
        findPreference(R.string.key_debug_facebook).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getActivity(), FacebookLogInActivity.class);
                startActivity(intent);
                return true;
            }
        });
        findPreference(R.string.key_debug_facebook_fetch_friends).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getActivity(), FacebookFriendsIntentService.class);
                getActivity().startService(intent);
                return true;
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private void startDateIntent() {
        Calendar cal = Calendar.getInstance();
        Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
        builder.appendPath("time");
        ContentUris.appendId(builder, cal.getTimeInMillis());
        Intent intent = new Intent(Intent.ACTION_VIEW)
                .setData(builder.build());

        startActivity(intent);
    }

    private final DatePickerDialog.OnDateSetListener onDailyReminderDateSelectedListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            int month1 = month + 1; // dialog picker months have 0 index
            dailyReminderDebugPreferences.setSelectedDate(dayOfMonth, month1, year);
        }
    };

}
