package com.alexstyl.specialdates.events.peopleevents;

import android.appwidget.AppWidgetManager;
import android.content.ContentResolver;
import android.content.Context;

import com.alexstyl.specialdates.CrashAndErrorTracker;
import com.alexstyl.specialdates.contact.ContactsProvider;
import com.alexstyl.specialdates.events.SettingsPresenter;
import com.alexstyl.specialdates.events.database.EventSQLiteOpenHelper;
import com.alexstyl.specialdates.events.namedays.NamedayDatabaseRefresher;
import com.alexstyl.specialdates.events.namedays.NamedayUserSettings;
import com.alexstyl.specialdates.events.namedays.calendar.resource.NamedayCalendarProvider;
import com.alexstyl.specialdates.upcoming.widget.list.UpcomingEventsScrollingWidgetView;
import com.alexstyl.specialdates.upcoming.widget.today.TodayUpcomingEventsView;
import com.alexstyl.specialdates.util.DateParser;
import com.alexstyl.specialdates.wear.WearSyncUpcomingEventsView;

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
    PeopleEventsProvider peopleEventsProvider(NamedayUserSettings namedayUserSettings,
                                              PeopleNamedaysCalculator peopleNamedaysCalculator,
                                              EventSQLiteOpenHelper sqLiteOpenHelper,
                                              ContactsProvider contactsProvider,
                                              CrashAndErrorTracker tracker) {
        AndroidPeopleEventsProvider androidPeopleEventsProvider = new AndroidPeopleEventsProvider(
                sqLiteOpenHelper,
                contactsProvider,
                new CustomEventProvider(context.getContentResolver()),
                tracker
        );
        // TODO extract into separate provider
        return new CompositePeopleEventsProvider(namedayUserSettings, peopleNamedaysCalculator, androidPeopleEventsProvider);
    }

    @Provides
    PeopleNamedaysCalculator peopleNamedayCalculator(NamedayUserSettings namedayPreferences,
                                                     NamedayCalendarProvider namedaysCalendarProvider,
                                                     ContactsProvider contactsProvider) {
        return new PeopleNamedaysCalculator(namedayPreferences, namedaysCalendarProvider, contactsProvider);
    }

    @Provides
    @Singleton
    UpcomingEventsViewRefresher peopleEventsViewRefresher(Context appContext, AppWidgetManager appWidgetManager) {
        return new UpcomingEventsViewRefresher(new HashSet<>(Arrays.asList(
                new WearSyncUpcomingEventsView(appContext),
                new TodayUpcomingEventsView(appContext, appWidgetManager),
                new UpcomingEventsScrollingWidgetView(appContext, appWidgetManager)
        )));
    }

    @Provides
    PeopleEventsStaticEventsRefresher peopleEventsStaticEventsRefresher(
            EventSQLiteOpenHelper eventSQlite,
            ContentResolver contentResolver,
            ContactsProvider contactsProvider,
            CrashAndErrorTracker tracker) {
        PeopleEventsRepository repository = new AndroidPeopleEventsRepository(contentResolver, contactsProvider, DateParser.INSTANCE, tracker);
        ContactEventsMarshaller marshaller = new ContactEventsMarshaller();
        PeopleEventsPersister androidPeopleEventsPersister = new AndroidPeopleEventsPersister(eventSQlite, marshaller, tracker);
        return new PeopleEventsStaticEventsRefresher(repository, androidPeopleEventsPersister);
    }

    @Provides
    NamedayDatabaseRefresher namedayDatabaseRefresher(NamedayUserSettings namedayUserSettings,
                                                      PeopleEventsPersister databaseProvider,
                                                      PeopleNamedaysCalculator calculator) {
        return new NamedayDatabaseRefresher(namedayUserSettings, databaseProvider, calculator);
    }

    @Provides
    PeopleEventsUpdater peopleEventsUpdater(PeopleEventsStaticEventsRefresher staticRefresher,
                                            NamedayDatabaseRefresher namedayRefresher,
                                            UpcomingEventsViewRefresher viewRefresher,
                                            UpcomingEventsSettings settings) {
        return new PeopleEventsUpdater(
                staticRefresher,
                namedayRefresher,
                viewRefresher,
                settings,
                Schedulers.io(),
                AndroidSchedulers.mainThread()
        );
    }

    @Provides
    PeopleEventsPersister peopleEventsPersister(CrashAndErrorTracker tracker, EventSQLiteOpenHelper helper) {
        ContactEventsMarshaller marshaller = new ContactEventsMarshaller();
        return new AndroidPeopleEventsPersister(helper, marshaller, tracker);
    }

    @Provides
    UpcomingEventsSettings eventPreferences() {
        return new AndroidUpcomingEventSettings(context);
    }

    @Provides
    SettingsPresenter peopleEventsDatabaseUpdater(UpcomingEventsViewRefresher uiRefresher, PeopleEventsUpdater peopleEventsUpdater) {
        return new SettingsPresenter(peopleEventsUpdater, uiRefresher, Schedulers.io());
    }
}
