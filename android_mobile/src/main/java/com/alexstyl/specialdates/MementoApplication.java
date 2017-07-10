package com.alexstyl.specialdates;

import android.app.AlarmManager;
import android.app.Application;
import android.content.Context;

import com.alexstyl.android.AlarmManagerCompat;
import com.alexstyl.specialdates.dailyreminder.DailyReminderPreferences;
import com.alexstyl.specialdates.dailyreminder.DailyReminderScheduler;
import com.alexstyl.specialdates.facebook.FacebookPreferences;
import com.alexstyl.specialdates.facebook.friendimport.FacebookFriendsScheduler;
import com.alexstyl.specialdates.images.AndroidContactsImageDownloader;
import com.alexstyl.specialdates.images.NutraBaseImageDecoder;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.L;
import com.novoda.notils.logger.simple.Log;

import net.danlew.android.joda.JodaTimeAndroid;

public class MementoApplication extends Application {

    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        initialiseDependencies();
        ErrorTracker.startTracking(this);

        DailyReminderPreferences preferences = DailyReminderPreferences.newInstance(this);
        if (preferences.isEnabled()) {
            AlarmManagerCompat alarmManager = AlarmManagerCompat.from(this);
            new DailyReminderScheduler(alarmManager, this).setupReminder(preferences);
        }
        if (FacebookPreferences.newInstance(this).isLoggedIn()) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            new FacebookFriendsScheduler(this, alarmManager).scheduleNext();

        }
    }

    protected void initialiseDependencies() {
        Log.setShowLogs(BuildConfig.DEBUG);
        JodaTimeAndroid.init(this);
        initImageLoader(this);
    }

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .threadPoolSize(10)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .imageDecoder(new NutraBaseImageDecoder(BuildConfig.DEBUG))
                .imageDownloader(new AndroidContactsImageDownloader(context));
        L.writeLogs(BuildConfig.DEBUG);
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config.build());
    }

}
