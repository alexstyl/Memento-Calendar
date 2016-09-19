package com.alexstyl.specialdates;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.service.DailyReminderIntentService;
import com.novoda.notils.logger.simple.Log;

import net.danlew.android.joda.JodaTimeAndroid;

public class MementoApplication extends Application {

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

    public static final String DEV_EMAIL = "alexstyl.dev@gmail.com";

    public static final String MARKET_LINK_SHORT = "http://goo.gl/ZQiAsi";

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        initialiseDependencies();
        ErrorTracker.startTracking(this);
        DailyReminderIntentService.setup(this);
    }

    protected void initialiseDependencies() {
        Log.setShowLogs(BuildConfig.DEBUG);
        JodaTimeAndroid.init(this);
        ImageLoader.init(this);
    }

}
