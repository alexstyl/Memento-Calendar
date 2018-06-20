package com.alexstyl.specialdates.events.peopleevents

import android.appwidget.AppWidgetManager
import android.content.ContentResolver
import android.content.Context
import com.alexstyl.specialdates.CrashAndErrorTracker
import com.alexstyl.specialdates.contact.ContactsProvider
import com.alexstyl.specialdates.date.DateParser
import com.alexstyl.specialdates.events.SettingsPresenter
import com.alexstyl.specialdates.events.database.EventSQLiteOpenHelper
import com.alexstyl.specialdates.events.namedays.NamedayDatabaseRefresher
import com.alexstyl.specialdates.events.namedays.NamedayUserSettings
import com.alexstyl.specialdates.events.namedays.calendar.resource.NamedayCalendarProvider
import com.alexstyl.specialdates.upcoming.widget.list.UpcomingEventsScrollingWidgetView
import com.alexstyl.specialdates.upcoming.widget.today.TodayUpcomingEventsView
import com.alexstyl.specialdates.wear.WearSyncUpcomingEventsView
import dagger.Module
import dagger.Provides
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Singleton

@Module
@Singleton
class PeopleEventsModule(private val context: Context) {

    @Provides
    fun peopleEventsProvider(peopleDynamicNamedaysProvider: PeopleDynamicNamedaysProvider,
                             androidPeopleEventsProvider: AndroidPeopleEventsProvider): PeopleEventsProvider {
        return CompositePeopleEventsProvider(listOf(peopleDynamicNamedaysProvider, androidPeopleEventsProvider))
    }

    @Provides
    fun androidPeopleEventsProvider(sqLiteOpenHelper: EventSQLiteOpenHelper,
                                    contactsProvider: ContactsProvider,
                                    dateParser: DateParser,
                                    tracker: CrashAndErrorTracker,
                                    shortLabelCreator: ShortDateLabelCreator): AndroidPeopleEventsProvider {
        return AndroidPeopleEventsProvider(
                sqLiteOpenHelper,
                contactsProvider,
                CustomEventProvider(context.contentResolver),
                dateParser,
                tracker,
                shortLabelCreator
        )
    }

    @Provides
    fun peopleNamedayCalculator(namedayPreferences: NamedayUserSettings,
                                namedaysCalendarProvider: NamedayCalendarProvider,
                                contactsProvider: ContactsProvider): PeopleDynamicNamedaysProvider {
        return PeopleDynamicNamedaysProvider(namedayPreferences, namedaysCalendarProvider, contactsProvider)
    }

    @Provides
    @Singleton
    fun peopleEventsViewRefresher(appContext: Context, appWidgetManager: AppWidgetManager): UpcomingEventsViewRefresher {
        return UpcomingEventsViewRefresher(mutableSetOf(
                WearSyncUpcomingEventsView(appContext),
                TodayUpcomingEventsView(appContext, appWidgetManager),
                UpcomingEventsScrollingWidgetView(appContext, appWidgetManager)
        ))
    }

    @Provides
    fun peopleEventsStaticEventsRefresher(
            eventSQlite: EventSQLiteOpenHelper,
            contentResolver: ContentResolver,
            contactsProvider: ContactsProvider,
            dateParser: DateParser,
            marshaller: ContactEventsMarshaller,
            tracker: CrashAndErrorTracker): PeopleEventsStaticEventsRefresher {
        val repository = AndroidPeopleEventsRepository(contentResolver, contactsProvider, dateParser, tracker)
        val androidPeopleEventsPersister = AndroidPeopleEventsPersister(eventSQlite, marshaller, tracker)
        return PeopleEventsStaticEventsRefresher(repository, androidPeopleEventsPersister)
    }

    @Provides
    fun namedayDatabaseRefresher(namedayUserSettings: NamedayUserSettings,
                                 databaseProvider: PeopleEventsPersister,
                                 provider: PeopleDynamicNamedaysProvider): NamedayDatabaseRefresher {
        return NamedayDatabaseRefresher(namedayUserSettings, databaseProvider, provider)
    }

    @Provides
    fun peopleEventsUpdater(staticRefresher: PeopleEventsStaticEventsRefresher,
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
    fun marshaller(dateLabelCreator: ShortDateLabelCreator) = ContactEventsMarshaller(dateLabelCreator)

    @Provides
    fun peopleEventsPersister(tracker: CrashAndErrorTracker,
                              marshaller: ContactEventsMarshaller,
                              helper: EventSQLiteOpenHelper): PeopleEventsPersister {
        return AndroidPeopleEventsPersister(helper, marshaller, tracker)
    }

    @Provides
    fun eventPreferences(): UpcomingEventsSettings {
        return AndroidUpcomingEventSettings(context)
    }

    @Provides
    fun peopleEventsDatabaseUpdater(uiRefresher: UpcomingEventsViewRefresher, peopleEventsUpdater: PeopleEventsUpdater): SettingsPresenter {
        return SettingsPresenter(peopleEventsUpdater, uiRefresher, Schedulers.io())
    }

    @Provides
    fun shortDateCreator() = ShortDateLabelCreator()


}
