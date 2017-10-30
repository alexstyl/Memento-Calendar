package com.alexstyl.specialdates.support;

import android.content.Context;

import com.alexstyl.specialdates.EasyPreferences;
import com.alexstyl.specialdates.R;

class CallForRatingPreferences {

    private EasyPreferences preferences;

    CallForRatingPreferences(Context context) {
        preferences = EasyPreferences.createForPrivatePreferences(context, R.string.pref_call_to_rate);
    }

    boolean hasUserRated() {
        return preferences.getBoolean(R.string.key_has_user_rated, false);
    }

    void setHasUserRated(boolean value) {
        preferences.setBoolean(R.string.key_has_user_rated, value);
    }

    long lastAskTimeAsked() {
        long previousTime = preferences.getLong(R.string.key_rate_previous_time_asked, -1);
        if (previousTime == -1) {
            preferences.setLong(R.string.key_rate_previous_time_asked, System.currentTimeMillis());
            previousTime = System.currentTimeMillis();
        }
        return previousTime;
    }

    void triggerNextTime() {
        preferences.setBoolean(R.string.key_rate_triggered, true);
    }

    void resetTrigger() {
        preferences.setBoolean(R.string.key_rate_triggered, false);
    }

    boolean isTriggered() {
        return preferences.getBoolean(R.string.key_rate_triggered, false);
    }

    void setLastAskTimedNow() {
        preferences.setLong(R.string.key_rate_previous_time_asked, System.currentTimeMillis());
    }
}
