package com.alexstyl.specialdates.events.peopleevents;

import android.appwidget.AppWidgetManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import com.alexstyl.specialdates.contact.ContactsProvider;
import com.alexstyl.specialdates.events.PeopleEventsDatabaseUpdater;
import com.alexstyl.specialdates.events.database.EventSQLiteOpenHelper;
import com.alexstyl.specialdates.events.namedays.NamedayUserSettings;
import com.alexstyl.specialdates.events.namedays.calendar.resource.NamedayCalendarProvider;
import com.alexstyl.specialdates.upcoming.widget.list.UpcomingEventsScrollingWidgetView;
import com.alexstyl.specialdates.upcoming.widget.today.TodayPeopleEventsView;
import com.alexstyl.specialdates.util.DateParser;
import com.alexstyl.specialdates.wear.WearSyncPeopleEventsView;

import javax.inject.Singleton;
import java.util.Arrays;
import java.util.HashSet;

import dagger.Module;
import dagger.Provides;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Module
@Singleton
public class PeopleEventsModule {

    private final Context context;

    public PeopleEventsModule(Context context) {
        this.context = context;
    }

    @Provides
    PeopleStaticEventsProvider peopleStaticEventsProvider(ContactsProvider contactsProvider) {
        return new AndroidPeopleStaticEventsProvider(
                new EventSQLiteOpenHelper(context),
                contactsProvider,
                new CustomEventProvider(context.getContentResolver())
        );
    }

    @Provides
    PeopleEventsProvider provider(NamedayUserSettings namedayUserSettings,
                                  PeopleNamedaysCalculator peopleNamedaysCalculator,
                                  PeopleStaticEventsProvider staticEvents) {
        return new PeopleEventsProvider(namedayUserSettings, peopleNamedaysCalculator, staticEvents);
    }

    @Provides
    PeopleNamedaysCalculator namedaysCalculator(NamedayUserSettings namedayPreferences,
                                                NamedayCalendarProvider namedaysCalendarProvider,
                                                ContactsProvider contactsProvider) {
        return new PeopleNamedaysCalculator(namedayPreferences, namedaysCalendarProvider, contactsProvider);
    }

    @Provides
    PeopleEventsViewRefresher viewRefresher(Context appContext, AppWidgetManager appWidgetManager) {
        return new PeopleEventsViewRefresher(new HashSet<>(Arrays.asList(
                new WearSyncPeopleEventsView(appContext),
                new TodayPeopleEventsView(appContext, appWidgetManager),
                new UpcomingEventsScrollingWidgetView(appContext, appWidgetManager)
        )));
    }

    @Provides
    DeviceEventsDatabaseRefresher refresher(SQLiteOpenHelper eventSQlite, ContentResolver contentResolver, ContactsProvider contactsProvider) {
        AndroidEventsRepository repository = new AndroidEventsRepository(contentResolver, contactsProvider, DateParser.INSTANCE);
        ContactEventsMarshaller marshaller = new ContactEventsMarshaller();
        PeopleEventsPersister peopleEventsPersister = new PeopleEventsPersister(eventSQlite);
        return new DeviceEventsDatabaseRefresher(repository, marshaller, peopleEventsPersister);
    }

    @Provides
    PeopleEventsDatabaseUpdater databaseUpdater(PeopleEventsViewRefresher uiRefresher, DeviceEventsDatabaseRefresher dbRefresher) {
        return new PeopleEventsDatabaseUpdater(dbRefresher, uiRefresher, Schedulers.io(), AndroidSchedulers.mainThread());
    }
}
