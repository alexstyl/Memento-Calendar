package com.alexstyl.specialdates.events.peopleevents;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.alexstyl.specialdates.ErrorTracker;
import com.alexstyl.specialdates.contact.ContactsProvider;
import com.alexstyl.specialdates.events.database.DatabaseContract;
import com.alexstyl.specialdates.events.database.EventColumns;
import com.alexstyl.specialdates.events.database.EventSQLiteOpenHelper;
import com.alexstyl.specialdates.events.database.PeopleEventsContract;
import com.alexstyl.specialdates.events.database.PeopleEventsContract.PeopleEvents;
import com.alexstyl.specialdates.events.namedays.NamedayDatabaseRefresher;
import com.alexstyl.specialdates.events.namedays.NamedayPreferences;
import com.alexstyl.specialdates.events.namedays.calendar.resource.NamedayCalendarProvider;
import com.alexstyl.specialdates.permissions.PermissionChecker;
import com.alexstyl.specialdates.util.DateParser;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class StaticEventsContentProvider extends ContentProvider {

    private static final int CODE_PEOPLE_EVENTS = 10;

    private EventSQLiteOpenHelper eventSQLHelper;
    private UriMatcher uriMatcher;

    private EventPreferences eventPreferences;
    private PeopleEventsPresenter presenter;
    private PeopleEventsUpdater peopleEventsUpdater;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        Resources resources = context.getResources();

        ContactsProvider contactsProvider = ContactsProvider.get(context);
        DateParser dateParser = DateParser.INSTANCE;
        AndroidEventsRepository repository = new AndroidEventsRepository(context.getContentResolver(), contactsProvider, dateParser);
        eventSQLHelper = new EventSQLiteOpenHelper(context);
        PeopleEventsPersister peopleEventsPersister = new PeopleEventsPersister(eventSQLHelper);
        NamedayPreferences namedayPreferences = NamedayPreferences.newInstance(context);
        ContactEventsMarshaller deviceEventsMarshaller = new ContactEventsMarshaller(EventColumns.SOURCE_DEVICE);
        NamedayCalendarProvider namedayCalendarProvider = NamedayCalendarProvider.newInstance(resources);
        PeopleNamedaysCalculator calculator = new PeopleNamedaysCalculator(namedayPreferences, namedayCalendarProvider, contactsProvider);
        PeopleEventsViewRefresher viewRefresher = PeopleEventsViewRefresher.get(context);

        eventPreferences = new EventPreferences(context);
        peopleEventsUpdater = new PeopleEventsUpdater(
                new PermissionChecker(context),
                new DeviceEventsDatabaseRefresher(repository, deviceEventsMarshaller, peopleEventsPersister),
                new NamedayDatabaseRefresher(namedayPreferences, peopleEventsPersister, deviceEventsMarshaller, calculator)
        );

        presenter = new PeopleEventsPresenter(
                AndroidSchedulers.mainThread(),
                EventsRefreshRequestsMonitor.newInstance(context),
                peopleEventsUpdater,
                viewRefresher
        );
        presenter.present();

        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PeopleEventsContract.AUTHORITY, PeopleEventsContract.PeopleEvents.PATH, CODE_PEOPLE_EVENTS);
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if (isAboutPeopleEvents(uri)) {
            if (!eventPreferences.hasBeenInitialised()) {
                peopleEventsUpdater.updateEvents();
                eventPreferences.markEventsAsInitialised();
            }

            UriQuery query = new UriQuery(uri, projection, selection, selectionArgs, sortOrder);
            SQLiteDatabase db = eventSQLHelper.getReadableDatabase();
            Cursor cursor = queryAnnualEvents(query, db);
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
            return cursor;
        }
        return emptyCursorWith(projection);
    }

    private MatrixCursor emptyCursorWith(String[] projection) {
        return new MatrixCursor(projection, 0);
    }

    private boolean isAboutPeopleEvents(Uri uri) {
        int uriType = uriMatcher.match(uri);
        return uriType == CODE_PEOPLE_EVENTS;
    }

    private Cursor queryAnnualEvents(UriQuery query, SQLiteDatabase db) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(DatabaseContract.AnnualEventsContract.TABLE_NAME);
        return builder.query(db, query.getProjection(), query.getSelection(), query.getSelectionArgs(), null, null, query.getSortOrder());
    }

    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case CODE_PEOPLE_EVENTS:
                return PeopleEvents.CONTENT_TYPE;
            default:
                return null;
        }
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        throwUnsupportedOperation("insert");
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        throwUnsupportedOperation("delete");
        return -1;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throwUnsupportedOperation("update");
        return -1;
    }

    @Override
    public void shutdown() {
        super.shutdown();
        presenter.stopPresenting();
    }

    private void throwUnsupportedOperation(String operation) {
        ErrorTracker.log(operation + " is not supported by the " + getClass().getSimpleName());
    }

}
