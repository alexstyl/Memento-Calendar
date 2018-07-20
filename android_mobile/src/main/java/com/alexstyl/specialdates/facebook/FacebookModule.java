package com.alexstyl.specialdates.facebook;

import android.content.Context;

import com.alexstyl.specialdates.CrashAndErrorTracker;
import com.alexstyl.specialdates.EasyPreferences;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.date.DateParser;
import com.alexstyl.specialdates.events.peopleevents.PeopleEventsPersister;
import com.alexstyl.specialdates.events.peopleevents.UpcomingEventsViewRefresher;
import com.alexstyl.specialdates.facebook.friendimport.CalendarURLCreator;
import com.alexstyl.specialdates.facebook.friendimport.ContactEventSerialiser;
import com.alexstyl.specialdates.facebook.friendimport.FacebookBirthdaysProvider;
import com.alexstyl.specialdates.facebook.friendimport.FacebookCalendarLoader;
import com.alexstyl.specialdates.facebook.friendimport.FacebookContactFactory;
import com.alexstyl.specialdates.facebook.friendimport.FacebookFriendsPersister;
import com.alexstyl.specialdates.facebook.friendimport.FacebookFriendsUpdater;
import com.alexstyl.specialdates.facebook.friendimport.FacebookFriendsUpdaterScheduler;

import dagger.Module;
import dagger.Provides;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Module
public class FacebookModule {

    private final Context context;

    public FacebookModule(Context context) {
        this.context = context;
    }

    @Provides
    FacebookUserSettings userSettings() {
        return new AndroidFacebookPreferences(EasyPreferences.Companion.createForPrivatePreferences(context, R.string.pref_facebook));
    }

    @Provides
    FacebookFriendsUpdaterScheduler scheduler() {
        return new FacebookFriendsUpdaterScheduler();
    }

    @Provides
    FacebookCalendarLoader calendarLoader() {
        return new FacebookCalendarLoader();
    }


    @Provides
    FacebookContactFactory contactFactory(DateParser dateParser) {
        return new FacebookContactFactory(dateParser);
    }


    @Provides
    ContactEventSerialiser serialiser(FacebookContactFactory factory, CrashAndErrorTracker tracker) {
        return new ContactEventSerialiser(factory, tracker);
    }

    @Provides
    FacebookBirthdaysProvider provider(FacebookCalendarLoader calendarLoader, ContactEventSerialiser serialiser) {
        return new FacebookBirthdaysProvider(calendarLoader, serialiser);
    }


    @Provides
    FacebookFriendsPersister persister(PeopleEventsPersister peoplePersister) {
        return new FacebookFriendsPersister(peoplePersister);
    }

    @Provides
    CalendarURLCreator creator(CrashAndErrorTracker tracker) {
        return new CalendarURLCreator(tracker);
    }

    @Provides
    FacebookFriendsUpdater updater(FacebookUserSettings facebookSettings,
                                   FacebookBirthdaysProvider facebookProvider,
                                   FacebookFriendsPersister facebookPersister,
                                   CalendarURLCreator calendarURLCreator,
                                   UpcomingEventsViewRefresher viewRefresher) {
        return new FacebookFriendsUpdater(facebookSettings,
                facebookProvider,
                facebookPersister,
                calendarURLCreator,
                viewRefresher,
                Schedulers.io(),
                AndroidSchedulers.mainThread()
        );
    }
}
