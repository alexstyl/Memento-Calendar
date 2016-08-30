package com.alexstyl.specialdates;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;

import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.service.DailyReminderIntentService;
import com.novoda.notils.logger.simple.Log;

import net.danlew.android.joda.JodaTimeAndroid;

public class MementoApp extends Application {

    private static final String TAG = "Memento";

    public static final String DEV_EMAIL = "alexstyl.dev@gmail.com";

    public static final String MARKET_LINK_SHORT = "http://goo.gl/ZQiAsi";

    private static Context context;

    public static String getVersionName(Context context) {
        String versionName = null;
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0
            );
            versionName = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            ErrorTracker.track(e);
        }
        return versionName;
    }

    public static Context getAppContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        initialiseDependencies();
        ErrorTracker.startTracking(this);
        DailyReminderIntentService.setup(this);
        handleVersionChanges();

    }

    protected void initialiseDependencies() {
        Log.setShowLogs(BuildConfig.DEBUG);
        JodaTimeAndroid.init(this);
        ImageLoader.init(this);
    }

    private void handleVersionChanges() {
        int previousCode = getLastAppVersion(this);
        if (previousCode == -1) {
            Log.d(TAG, "Initial Boot of app. Last Version was " + previousCode);

            try {
                PackageInfo info = getPackageManager().getPackageInfo(this.getPackageName(), 0);
                setLastAppVersion(this, info.versionCode);
                Log.d(TAG, "Set Last Version to " + info.versionCode);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private static int getLastAppVersion(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(context.getString(R.string.prev_app_version), -1);
    }

    private static void setLastAppVersion(Context context, int version) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit().putInt(context.getString(R.string.prev_app_version), version);
    }
}
