package com.alexstyl.specialdates;

import android.content.Context;

import com.alexstyl.specialdates.events.namedays.NamedayLocale;
import com.crashlytics.android.Crashlytics;
import com.novoda.notils.logger.simple.Log;

import java.util.Locale;

import io.fabric.sdk.android.Fabric;

public final class FabricTracker implements CrashAndErrorTracker {

    private static final String TAG = FabricTracker.class.getSimpleName();
    private static final String KEY_LOCALE = "user_locale";
    private static final String KEY_NAMEDAY_LOCALE = "nameday_locale";
    private static final String NONE = "no_locale";

    private static boolean hasBeenInitialised = false;

    private final Context context;

    public FabricTracker(Context context) {
        this.context = context;
    }

    @Override
    public void startTracking() {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Ignoring Crash Tracking in DEBUG builds");
            return;
        }

        Fabric.with(context, new Crashlytics());

        Log.i(TAG, "Crashlytics tracking started ");
        hasBeenInitialised = true;
        Crashlytics.setString(KEY_LOCALE, String.valueOf(Locale.getDefault()));
    }

    @Override
    public void track(Exception e) {
        if (hasBeenInitialised) {
            Crashlytics.logException(e);
        }
        Log.w(e);
    }

    @Override
    public void onNamedayLocaleChanged(NamedayLocale locale) {
        if (hasBeenInitialised) {
            Crashlytics.setString(KEY_NAMEDAY_LOCALE, locale == null ? NONE : locale.name());
        }
    }

    @Override
    public void updateLocaleUsed() {
        if (hasBeenInitialised) {
            Crashlytics.setString(KEY_LOCALE, String.valueOf(Locale.getDefault()));
        }
    }

    @Override
    public void log(String message) {
        if (hasBeenInitialised) {
            Crashlytics.log(message);
        }
    }
}

