package com.alexstyl.specialdates.facebook.friendimport;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.alexstyl.specialdates.BuildConfig;
import com.alexstyl.specialdates.CrashAndErrorTracker;
import com.alexstyl.specialdates.MementoApplication;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.events.database.EventSQLiteOpenHelper;
import com.alexstyl.specialdates.events.peopleevents.ContactEventsMarshaller;
import com.alexstyl.specialdates.events.peopleevents.AndroidPeopleEventsPersister;
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsViewRefresher;
import com.alexstyl.specialdates.facebook.FacebookPreferences;
import com.alexstyl.specialdates.facebook.UserCredentials;

import javax.inject.Inject;
import java.net.URL;
import java.util.List;

public class FacebookFriendsIntentService extends IntentService {
    private static final String TAG = FacebookFriendsIntentService.class.getSimpleName();
    private static final int NOTIFICATION_ID = 123;

    @Inject PeopleEventsViewRefresher uiRefresher;
    @Inject CrashAndErrorTracker tracker;

    public FacebookFriendsIntentService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ((MementoApplication) getApplication()).getApplicationModule().inject(this);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        FacebookCalendarLoader calendarLoader = new FacebookCalendarLoader();
        FacebookContactFactory factory = new FacebookContactFactory();
        ContactEventSerialiser serialiser = new ContactEventSerialiser(factory, tracker);
        FacebookBirthdaysProvider calendarFetcher = new FacebookBirthdaysProvider(calendarLoader, serialiser);

        FacebookPreferences preferences = FacebookPreferences.newInstance(this);
        UserCredentials userCredentials = preferences.retrieveCredentials();
        if (isAnnonymous(userCredentials)) {
            tracker.track(new RuntimeException("Tried to fetch events, but was anonymous"));
            return;
        }
        CalendarURLCreator calendarURLCreator = new CalendarURLCreator(tracker);

        URL calendarUrl = calendarURLCreator.createFrom(userCredentials);
        ContactEventsMarshaller marshaller = new ContactEventsMarshaller();
        FacebookFriendsPersister persister = new FacebookFriendsPersister(
                new AndroidPeopleEventsPersister(
                        new EventSQLiteOpenHelper(this),
                        marshaller,
                        tracker
                )
        );
        try {
            List<ContactEvent> friends = calendarFetcher.fetchCalendarFrom(calendarUrl);
            persister.keepOnly(friends);
            uiRefresher.refreshViews();
        } catch (CalendarFetcherException e) {
            tracker.track(e);
        }

        if (BuildConfig.DEBUG) {
            notifyServiceRan();
        }
    }

    private void notifyServiceRan() {
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Friends fetched")
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).notify(NOTIFICATION_ID, notification);
    }

    private boolean isAnnonymous(UserCredentials userCredentials) {
        return UserCredentials.ANNONYMOUS.equals(userCredentials);
    }
}
