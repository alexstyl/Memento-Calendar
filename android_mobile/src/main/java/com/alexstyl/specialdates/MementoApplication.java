package com.alexstyl.specialdates;

import android.app.AlarmManager;
import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.alexstyl.resources.ResourcesModule;
import com.alexstyl.specialdates.dailyreminder.DailyReminderScheduler;
import com.alexstyl.specialdates.dailyreminder.DailyReminderUserSettings;
import com.alexstyl.specialdates.events.namedays.activity.NamedaysInADayModule;
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsModule;
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsUpdater;
import com.alexstyl.specialdates.events.peopleevents.UpcomingEventsSettings;
import com.alexstyl.specialdates.facebook.FacebookModule;
import com.alexstyl.specialdates.facebook.FacebookUserSettings;
import com.alexstyl.specialdates.facebook.friendimport.FacebookFriendsScheduler;
import com.alexstyl.specialdates.images.AndroidContactsImageDownloader;
import com.alexstyl.specialdates.images.ImageModule;
import com.alexstyl.specialdates.images.NutraBaseImageDecoder;
import com.alexstyl.specialdates.permissions.MementoPermissions;
import com.alexstyl.specialdates.theming.ThemingModule;
import com.alexstyl.specialdates.ui.widget.ViewModule;
import com.evernote.android.job.DailyJob;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.L;
import com.novoda.notils.logger.simple.Log;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

import net.danlew.android.joda.JodaTimeAndroid;

public class MementoApplication extends MultiDexApplication {

    private AppComponent appComponent;

    @Inject CrashAndErrorTracker tracker;
    @Inject FacebookUserSettings facebookSettings;
    @Inject UpcomingEventsJobCreator jobCreator;
    @Inject PeopleEventsUpdater peopleEventsUpdater;
    @Inject MementoPermissions permissions;
    @Inject UpcomingEventsSettings settings;
    @Inject DailyReminderUserSettings dailyReminderUserSettings;
    @Inject DailyReminderScheduler dailyReminderScheduler;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent =
                DaggerAppComponent.builder()
                        .androidApplicationModule(new AndroidApplicationModule(this))
                        .resourcesModule(new ResourcesModule(this, getResources()))
                        .imageModule(new ImageModule(getResources()))
                        .peopleEventsModule(new PeopleEventsModule(this))
                        .themingModule(new ThemingModule())
                        .viewModule(new ViewModule(getResources()))
                        .facebookModule(new FacebookModule(this))
                        .namedaysInADayModule(new NamedaysInADayModule())
                        .build();

        appComponent.inject(this);

        initialiseDependencies();
        tracker.startTracking();

        if (dailyReminderUserSettings.isEnabled()) {
            dailyReminderScheduler.setupReminder(dailyReminderUserSettings); // TODO use job scheduler
        }

        if (facebookSettings.isLoggedIn()) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            new FacebookFriendsScheduler(this, alarmManager).scheduleNext();// TODO use job scheduler
        }

        if (needsToInitialiseEvents()) {
            peopleEventsUpdater
                    .updateEvents()
                    .subscribe();
        }

        schedulePeopleEventJob();

    }

    private boolean needsToInitialiseEvents() {
        return permissions.canReadAndWriteContacts() && !settings.hasBeenInitialised();
    }

    private void schedulePeopleEventJob() {
        JobManager.create(this).addJobCreator(jobCreator);
        DailyJob.schedule(
                new JobRequest.Builder("ANY_TAG"),
                TimeUnit.HOURS.toMillis(1),
                TimeUnit.HOURS.toMillis(3)
        );

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
