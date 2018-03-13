package com.alexstyl.specialdates.events.peopleevents

import android.appwidget.AppWidgetManager
import android.content.ContentResolver
import android.content.Context
import com.alexstyl.specialdates.CrashAndErrorTracker
import com.alexstyl.specialdates.UpcomingEventsView
import com.alexstyl.specialdates.contact.ContactsProvider
import com.alexstyl.specialdates.events.SettingsPresenter
import com.alexstyl.specialdates.events.database.EventSQLiteOpenHelper
import com.alexstyl.specialdates.events.namedays.NamedayDatabaseRefresher
import com.alexstyl.specialdates.events.namedays.NamedayUserSettings
import com.alexstyl.specialdates.events.namedays.calendar.resource.NamedayCalendarProvider
import com.alexstyl.specialdates.upcoming.widget.list.UpcomingEventsScrollingWidgetView
import com.alexstyl.specialdates.upcoming.widget.today.TodayUpcomingEventsView
import com.alexstyl.specialdates.util.DateParser
import com.alexstyl.specialdates.wear.WearSyncUpcomingEventsView
import dagger.Module
import dagger.Provides
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import javax.inject.Singleton

@Module
@Singleton
open class PeopleEventsModule(private val context: Context) {

    @Provides
    open fun peopleEventsProvider(peopleDynamicNamedaysProvider: PeopleDynamicNamedaysProvider,
                                  androidPeopleEventsProvider: AndroidPeopleEventsProvider): PeopleEventsProvider {
        return CompositePeopleEventsProvider(listOf(peopleDynamicNamedaysProvider, androidPeopleEventsProvider))
    }

    @Provides
    open fun androidPeopleEventsProvider(sqLiteOpenHelper: EventSQLiteOpenHelper, contactsProvider: ContactsProvider, tracker: CrashAndErrorTracker): AndroidPeopleEventsProvider {
        return AndroidPeopleEventsProvider(
                sqLiteOpenHelper,
                contactsProvider,
                CustomEventProvider(context.contentResolver),
                tracker
        )
    }

    @Provides
    open fun peopleNamedayCalculator(namedayPreferences: NamedayUserSettings,
                                namedaysCalendarProvider: NamedayCalendarProvider,
                                contactsProvider: ContactsProvider): PeopleDynamicNamedaysProvider {
        return PeopleDynamicNamedaysProvider(namedayPreferences, namedaysCalendarProvider, contactsProvider)
    }

    @Provides
    @Singleton
    open fun peopleEventsViewRefresher(appContext: Context, appWidgetManager: AppWidgetManager): UpcomingEventsViewRefresher {
        return UpcomingEventsViewRefresher(HashSet(Arrays.asList<UpcomingEventsView>(
                WearSyncUpcomingEventsView(appContext),
                TodayUpcomingEventsView(appContext, appWidgetManager),
                UpcomingEventsScrollingWidgetView(appContext, appWidgetManager)
        )))
    }

    @Provides
    open fun peopleEventsStaticEventsRefresher(
            eventSQlite: EventSQLiteOpenHelper,
            contentResolver: ContentResolver,
            contactsProvider: ContactsProvider,
            tracker: CrashAndErrorTracker): PeopleEventsStaticEventsRefresher {
        val repository = AndroidPeopleEventsRepository(contentResolver, contactsProvider, DateParser.INSTANCE, tracker)
        val marshaller = ContactEventsMarshaller()
        val androidPeopleEventsPersister = AndroidPeopleEventsPersister(eventSQlite, marshaller, tracker)
        return PeopleEventsStaticEventsRefresher(repository, androidPeopleEventsPersister)
    }

    @Provides
    open fun namedayDatabaseRefresher(namedayUserSettings: NamedayUserSettings,
                                      databaseProvider: PeopleEventsPersister,
                                      provider: PeopleDynamicNamedaysProvider): NamedayDatabaseRefresher {
        return NamedayDatabaseRefresher(namedayUserSettings, databaseProvider, provider)
    }

    @Provides
    open fun peopleEventsUpdater(staticRefresher: PeopleEventsStaticEventsRefresher,
                            namedayRefresher: NamedayDatabaseRefresher,
                            viewRefresher: UpcomingEventsViewRefresher,
                            settings: UpcomingEventsSettings): PeopleEventsUpdater {
        return PeopleEventsUpdater(
                staticRefresher,
                namedayRefresher,
                viewRefresher,
                settings,
                Schedulers.io(),
                AndroidSchedulers.mainThread()
        )
    }

    @Provides
    open fun peopleEventsPersister(tracker: CrashAndErrorTracker, helper: EventSQLiteOpenHelper): PeopleEventsPersister {
        val marshaller = ContactEventsMarshaller()
        return AndroidPeopleEventsPersister(helper, marshaller, tracker)
    }

    @Provides
    open fun eventPreferences(): UpcomingEventsSettings {
        return AndroidUpcomingEventSettings(context)
    }

    @Provides
    open fun peopleEventsDatabaseUpdater(uiRefresher: UpcomingEventsViewRefresher, peopleEventsUpdater: PeopleEventsUpdater): SettingsPresenter {
        return SettingsPresenter(peopleEventsUpdater, uiRefresher, Schedulers.io())
    }
}
