package com.alexstyl.specialdates.debug;

import android.content.Context;
import android.support.annotation.StringRes;

import com.alexstyl.specialdates.EasyPreferences;

final class DebugPreferences {

    private final EasyPreferences preferences;

    public static DebugPreferences newInstance(Context context, @StringRes int prefKey) {
        return new DebugPreferences(EasyPreferences.createForPrivatePreferences(context, prefKey));
    }

    private DebugPreferences(EasyPreferences preferences) {
        this.preferences = preferences;
    }

    void wipe() {
        preferences.clear();
    }
}
