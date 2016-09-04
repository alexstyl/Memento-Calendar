package com.alexstyl.specialdates.analytics;

import android.content.Context;

import com.google.firebase.analytics.FirebaseAnalytics;

public class Analytics {

    private final FirebaseAnalytics firebaseAnalytics;

    private static Analytics INSTANCE;

    public static Analytics get(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new Analytics(FirebaseAnalytics.getInstance(context));
        }
        return INSTANCE;
    }

    public Analytics(FirebaseAnalytics firebaseAnalytics) {
        this.firebaseAnalytics = firebaseAnalytics;
    }

    public void track(AnalyticsEvent event) {
        firebaseAnalytics.logEvent(event.getName(), event.data());
    }

}
