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

    private Firebase(FirebaseAnalytics firebaseAnalytics) {
        this.firebaseAnalytics = firebaseAnalytics;
    }

    @Override
    public void trackAction(Action goToToday) {
        String actionName = goToToday.getName();
        firebaseAnalytics.logEvent(actionName, NO_DATA);
        Log.d("Tracking event:" + actionName);
    }

    @Override
    public void trackAction(ActionWithParameters action) {
        String formattedAction = format(action);
        firebaseAnalytics.logEvent(formattedAction, NO_DATA);
        Log.d("Tracking event:" + formattedAction);
    }

    @Override
    public void trackScreen(Screen screen) {
        firebaseAnalytics.logEvent("screen_view:" + screen.screenName(), NO_DATA);
        Log.d("Tracking screen_view:" + screen);
    }

    static String format(ActionWithParameters action) {
        return String.format(Locale.US, "%s_%s_%s", action.getName(), action.getLabel(), action.getValue());
    }
}
