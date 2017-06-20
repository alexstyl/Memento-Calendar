package com.alexstyl.specialdates.facebook.friendimport;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.alexstyl.specialdates.BuildConfig;
import com.alexstyl.specialdates.ErrorTracker;
import com.alexstyl.specialdates.ExternalWidgetRefresher;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.events.database.EventSQLiteOpenHelper;
import com.alexstyl.specialdates.events.peopleevents.ContactEventsMarshaller;
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsPersister;
import com.alexstyl.specialdates.facebook.FacebookPreferences;
import com.alexstyl.specialdates.facebook.UserCredentials;

import java.net.URL;
import java.util.List;

import static com.alexstyl.specialdates.events.database.EventColumns.SOURCE_FACEBOOK;

public class FacebookFriendsIntentService extends IntentService {
    private static final String TAG = FacebookFriendsIntentService.class.getSimpleName();

    public FacebookFriendsIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        FacebookCalendarLoader calendarLoader = new FacebookCalendarLoader();
        FacebookContactFactory factory = new FacebookContactFactory();
        ContactEventSerialiser serialiser = new ContactEventSerialiser(factory);
        FacebookBirthdaysProvider calendarFetcher = new FacebookBirthdaysProvider(calendarLoader, serialiser);

        FacebookPreferences preferences = FacebookPreferences.newInstance(this);
        UserCredentials userCredentials = preferences.retrieveCredentials();
        if (isAnnonymous(userCredentials)) {
            ErrorTracker.track(new RuntimeException("Tried to fetch events, but was anonymous"));
            return;
        }
        CalendarURLCreator calendarURLCreator = new CalendarURLCreator();

        URL calendarUrl = calendarURLCreator.createFrom(userCredentials);
        ContactEventsMarshaller marshaller = new ContactEventsMarshaller(SOURCE_FACEBOOK);
        FacebookFriendsPersister persister = new FacebookFriendsPersister(new PeopleEventsPersister(getContentResolver(), new EventSQLiteOpenHelper(this)), marshaller);
        try {
            List<ContactEvent> friends = calendarFetcher.fetchCalendarFrom(calendarUrl);
            persister.keepOnly(friends);
        } catch (CalendarFetcherException e) {
            ErrorTracker.track(e);
        }

        ExternalWidgetRefresher.get(this).refreshAllWidgets();

        if (BuildConfig.DEBUG) {
            notifyServiceRan();
        }
    }

    private void notifyServiceRan() {
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Friends fetched")
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).notify(123, notification);
    }

    private boolean isAnnonymous(UserCredentials userCredentials) {
        return UserCredentials.ANNONYMOUS.equals(userCredentials);
    }
}
