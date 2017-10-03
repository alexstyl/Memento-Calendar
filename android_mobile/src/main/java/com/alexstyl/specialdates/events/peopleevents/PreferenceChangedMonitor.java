package com.alexstyl.specialdates.events.peopleevents;

import android.content.SharedPreferences;
import android.content.res.Resources;

import com.alexstyl.specialdates.EasyPreferences;
import com.alexstyl.specialdates.Monitor;

import java.util.ArrayList;
import java.util.List;

class PreferenceChangedMonitor implements Monitor {

    private final EasyPreferences preferences;
    private final List<String> keys;

    private SharedPreferences.OnSharedPreferenceChangeListener listener;

    PreferenceChangedMonitor(EasyPreferences preferences, Resources strings, int firstKeys, int... keys) {
        this.preferences = preferences;
        this.keys = new ArrayList<>(keys.length + 1);
        this.keys.add(strings.getString(firstKeys));
        for (int key : keys) {
            this.keys.add(strings.getString(key));
        }
    }

    @Override
    public void startObserving(final Callback callback) {
        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (isAKeyIcareAbout(key)) {
                    callback.onMonitorTriggered();
                }
            }

        };
        preferences.addOnPreferenceChangedListener(listener);
    }

    private boolean isAKeyIcareAbout(String key) {
        return keys.contains(key);
    }

    @Override
    public void stopObserving() {
        preferences.removeOnPreferenceChagnedListener(listener);
    }
}
