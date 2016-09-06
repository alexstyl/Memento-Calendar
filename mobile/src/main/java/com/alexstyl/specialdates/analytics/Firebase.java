package com.alexstyl.specialdates.analytics;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.novoda.notils.logger.simple.Log;

import java.util.Locale;

public class Firebase implements Analytics {

    private static final Bundle NO_DATA = null;

    private final FirebaseAnalytics firebaseAnalytics;

    private static Firebase INSTANCE;

    public static Firebase get(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new Firebase(FirebaseAnalytics.getInstance(context));
        }
        return INSTANCE;
    }

    public Firebase(FirebaseAnalytics firebaseAnalytics) {
        this.firebaseAnalytics = firebaseAnalytics;
    }

    @Override
    public void trackAction(AnalyticsAction action) {
        String formattedAction = format(action);
        firebaseAnalytics.logEvent(formattedAction, NO_DATA);
        Log.d("Tracking event:" + formattedAction);
    }

    @Override
    public void trackScreen(Screen screen) {
        firebaseAnalytics.logEvent("screen_view:" + screen.screenName(), NO_DATA);
        Log.d("Tracking screen_view:" + screen);
    }

    private String format(AnalyticsAction action) {
        return String.format(Locale.US, "%s:%s:%s", action.getName(), action.getLabel(), action.getValue());
    }
}
