package com.alexstyl.specialdates.events.peopleevents;

import android.content.Context;
import android.content.res.Resources;

import com.alexstyl.specialdates.EasyPreferences;
import com.alexstyl.specialdates.Monitor;
import com.alexstyl.specialdates.R;

final class PeopleSettingsMonitor implements Monitor {

    private final Monitor monitor;

    public static Monitor newInstance(Context context, Resources resources) {
        PreferenceChangedMonitor monitor = new PreferenceChangedMonitor(
                EasyPreferences.createForDefaultPreferences(context),
                resources,
                R.string.key_enable_namedays,
                R.string.key_nameday_lang,
                R.string.key_namedays_full_name
        );
        return new PeopleSettingsMonitor(monitor);
    }

    private PeopleSettingsMonitor(Monitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public void startObserving(Callback callback) {
        monitor.startObserving(callback);
    }

    @Override
    public void stopObserving() {
        monitor.stopObserving();
    }
}
