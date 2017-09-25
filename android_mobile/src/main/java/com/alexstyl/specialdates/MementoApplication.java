package com.alexstyl.specialdates;

import android.app.AlarmManager;
import android.app.Application;
import android.content.Context;

import com.alexstyl.android.AlarmManagerCompat;
import com.alexstyl.resources.ResourcesModule;
import com.alexstyl.specialdates.dailyreminder.DailyReminderPreferences;
import com.alexstyl.specialdates.dailyreminder.DailyReminderScheduler;
import com.alexstyl.specialdates.events.namedays.activity.NamedaysInADayModule;
import com.alexstyl.specialdates.facebook.FacebookPreferences;
import com.alexstyl.specialdates.facebook.friendimport.FacebookFriendsScheduler;
import com.alexstyl.specialdates.images.AndroidContactsImageDownloader;
import com.alexstyl.specialdates.images.ImageModule;
import com.alexstyl.specialdates.images.NutraBaseImageDecoder;
import com.alexstyl.specialdates.ui.widget.ViewModule;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.L;
import com.novoda.notils.logger.simple.Log;

import net.danlew.android.joda.JodaTimeAndroid;

public class MementoApplication extends Application {

    private AppComponent appComponent;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        appComponent =
                DaggerAppComponent.builder()
                        .appModule(new AppModule(this))
                        .resourcesModule(new ResourcesModule(getResources()))
                        .imageModule(new ImageModule(getResources()))
                        .viewModule(new ViewModule(getResources()))
                        .namedaysInADayModule(new NamedaysInADayModule())
                        .build();
    }

    @Override
    public void onCreate() {
        super.onCreate();

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

    @SuppressWarnings("MagicNumber")
    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.MIN_PRIORITY)
                .threadPoolSize(10)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .imageDecoder(new NutraBaseImageDecoder(BuildConfig.DEBUG))
                .imageDownloader(new AndroidContactsImageDownloader(context));
        L.writeLogs(BuildConfig.DEBUG);
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config.build());
    }

    public AppComponent getApplicationModule() {
        return appComponent;
    }
}
