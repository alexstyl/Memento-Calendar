package com.alexstyl.specialdates.events.peopleevents;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.alexstyl.specialdates.contact.ContactsProvider;
import com.alexstyl.specialdates.events.database.ContactColumns;
import com.alexstyl.specialdates.events.database.EventSQLiteOpenHelper;
import com.alexstyl.specialdates.events.database.EventsDBContract.AnnualEventsContract;
import com.alexstyl.specialdates.events.database.PeopleEventsContract;
import com.alexstyl.specialdates.events.database.PeopleEventsContract.PeopleEvents;
import com.alexstyl.specialdates.events.namedays.NamedayDatabaseRefresher;
import com.alexstyl.specialdates.events.namedays.NamedayPreferences;
import com.alexstyl.specialdates.events.namedays.calendar.resource.NamedayCalendarProvider;
import com.alexstyl.specialdates.permissions.PermissionChecker;
import com.alexstyl.specialdates.upcoming.NamedaySettingsMonitor;
import com.alexstyl.specialdates.util.ContactsObserver;
import com.alexstyl.specialdates.util.DateParser;
import com.novoda.notils.exception.DeveloperError;

public class PeopleEventsContentProvider extends ContentProvider {

    private static final int CODE_PEOPLE_EVENTS = 10;
    private EventSQLiteOpenHelper eventSQLHelper;
    private UriMatcher uriMatcher;
    private PeopleEventsUpdater peopleEventsUpdater;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        Resources resources = context.getResources();
        ContentResolver contentResolver = context.getContentResolver();

        ContactsProvider contactsProvider = ContactsProvider.get(context);
        DateParser dateParser = DateParser.INSTANCE;
        PeopleEventsRepository repository = new PeopleEventsRepository(contentResolver, contactsProvider, dateParser);
        eventSQLHelper = new EventSQLiteOpenHelper(context);
        PeopleEventsPersister peopleEventsPersister = new PeopleEventsPersister(eventSQLHelper);
        NamedayPreferences namedayPreferences = NamedayPreferences.newInstance(context);
        ContactEventsMarshaller mementoMarshaller = new ContactEventsMarshaller(ContactColumns.SOURCE_MEMENTO);
        NamedayCalendarProvider namedayCalendarProvider = NamedayCalendarProvider.newInstance(resources);
        PeopleNamedaysCalculator calculator = new PeopleNamedaysCalculator(namedayPreferences, namedayCalendarProvider, contactsProvider);
        ContactEventsMarshaller marshaller = new ContactEventsMarshaller(ContactColumns.SOURCE_DEVICE);
        peopleEventsUpdater = new PeopleEventsUpdater(
                new PeopleEventsDatabaseRefresher(repository, marshaller, peopleEventsPersister),
                new NamedayDatabaseRefresher(namedayPreferences, peopleEventsPersister, mementoMarshaller, calculator),
                new EventPreferences(context),
                new ContactsObserver(contentResolver, new Handler()),
                new NamedaySettingsMonitor(namedayPreferences),
                new PermissionChecker(context)
        );
        peopleEventsUpdater.register();
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PeopleEventsContract.AUTHORITY, PeopleEventsContract.PeopleEvents.PATH, CODE_PEOPLE_EVENTS);
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        peopleEventsUpdater.updateEventsIfNeeded();

        if (isAboutPeopleEvents(uri)) {
            UriQuery query = new UriQuery(uri, projection, selection, selectionArgs, sortOrder);
            SQLiteDatabase db = eventSQLHelper.getReadableDatabase();
            Cursor cursor = queryAnnualEvents(query, db);
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
            return cursor;
        }
        return null;
    }

    private boolean isAboutPeopleEvents(Uri uri) {
        int uriType = uriMatcher.match(uri);
        return uriType == CODE_PEOPLE_EVENTS;
    }

    private Cursor queryAnnualEvents(UriQuery query, SQLiteDatabase db) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(AnnualEventsContract.TABLE_NAME);
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

    private void throwUnsupportedOperation(String insert) {
        throw new DeveloperError(insert + " is not supported by the " + getClass().getSimpleName());
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

}
