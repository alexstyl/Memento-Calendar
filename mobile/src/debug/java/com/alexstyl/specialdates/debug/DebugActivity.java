package com.alexstyl.specialdates.debug;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.provider.CalendarContract;
import android.widget.Toast;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.contact.actions.IntentAction;
import com.alexstyl.specialdates.service.DailyReminderIntentService;
import com.alexstyl.specialdates.ui.base.MementoPreferenceActivity;
import com.alexstyl.specialdates.ui.base.MementoPreferenceFragment;
import com.alexstyl.specialdates.util.Utils;
import com.alexstyl.specialdates.widgetprovider.TodayWidgetProvider;

import java.util.Calendar;

public class DebugActivity extends MementoPreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_debug);
    }

    public static class DebugFragment extends MementoPreferenceFragment {

        @Override
        public void onCreate(Bundle paramBundle) {
            super.onCreate(paramBundle);
            addPreferencesFromResource(R.xml.preference_debug);

            findPreference(R.string.key_debug_refresh_widget).setOnPreferenceClickListener(
                    new Preference.OnPreferenceClickListener() {
                        @Override
                        public boolean onPreferenceClick(Preference preference) {
                            TodayWidgetProvider.updateWidgets(getActivity());
                            Toast.makeText(getActivity(), "Widget(s) refreshed", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }
            );

            findPreference(R.string.key_debug_daily_reminder).setOnPreferenceClickListener(
                    new Preference.OnPreferenceClickListener() {
                        @Override
                        public boolean onPreferenceClick(Preference preference) {
                            DailyReminderIntentService.startService(getActivity());
                            Toast.makeText(getActivity(), "Daily Reminder Triggered", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }
            );

            findPreference(R.string.key_debug_start_calendar).setOnPreferenceClickListener(
                    new Preference.OnPreferenceClickListener() {
                        @Override
                        public boolean onPreferenceClick(Preference preference) {
                            startDateIntent();
                            return true;
                        }

                    }
            );
        }

        private void startDateIntent() {
            IntentAction i = new IntentAction() {
                @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
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
            Utils.openIntentSafely(getActivity(), i);
        }
    }

}
