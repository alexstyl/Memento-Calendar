package com.alexstyl.specialdates.upcoming;

import android.content.SharedPreferences;

import com.alexstyl.android.preferences.PreferenceKeyId;
import com.alexstyl.resources.Strings;
import com.alexstyl.specialdates.R;

public class UpcomingEventsSettingsMonitor {

    private final SharedPreferences sharedPreferences;
    private final Strings strings;
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;

    UpcomingEventsSettingsMonitor(SharedPreferences sharedPreferences, Strings strings) {
        this.sharedPreferences = sharedPreferences;
        this.strings = strings;
    }

    public void register(Listener listener) {
        preferenceChangeListener = createPreferenceChangedListenerFrom(listener);
        sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    private SharedPreferences.OnSharedPreferenceChangeListener createPreferenceChangedListenerFrom(final Listener listener) {
        return new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (isAEventChangedEvent(key)) {
                    listener.onSettingUpdated();
                }
            }

            private boolean isAEventChangedEvent(String key) {
                return isKey(key, R.string.key_enable_bank_holidays)
                        || isKey(key, R.string.key_bankholidays_language)
                        || isKey(key, R.string.key_enable_namedays)
                        || isKey(key, R.string.key_nameday_lang)
                        || isKey(key, R.string.key_namedays_full_name)
                        || isKey(key, R.string.key_namedays_contacts_only);
            }

            private boolean isKey(String key, @PreferenceKeyId int keyId) {
                return strings.getString(keyId).equalsIgnoreCase(key);
            }
        };
    }

    public void unregister() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    public interface Listener {
        void onSettingUpdated();
    }
}
