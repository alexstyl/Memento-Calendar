package com.alexstyl.specialdates.events.peopleevents;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.alexstyl.specialdates.events.database.EventSQLiteOpenHelper;
import com.alexstyl.specialdates.events.database.EventsDBContract;
import com.alexstyl.specialdates.events.database.EventsDBContract.AnnualEventsContract;
import com.alexstyl.specialdates.events.database.PeopleEventsContract;
import com.alexstyl.specialdates.events.database.PeopleEventsContract.PeopleEvents;
import com.novoda.notils.exception.DeveloperError;

public class PeopleEventsContentProvider extends ContentProvider {

    private static final int CODE_PEOPLE_EVENTS = 10;
    private EventSQLiteOpenHelper eventSQLHelper;
    private UriMatcher uriMatcher;
    private PeopleEventsUpdater peopleEventsUpdater;

    @Override
    public boolean onCreate() {
        eventSQLHelper = new EventSQLiteOpenHelper(getContext());
        peopleEventsUpdater = PeopleEventsUpdater.newInstance(getContext());
        peopleEventsUpdater.register();
        initialiseMatcher();
        return true;
    }

    private void initialiseMatcher() {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PeopleEventsContract.AUTHORITY, PeopleEventsContract.PeopleEvents.PATH, CODE_PEOPLE_EVENTS);
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        peopleEventsUpdater.updateEventsIfNeeded();

        if (isAboutPeopleEvents(uri)) {
            UriQuery query = new UriQuery(uri, projection, selection, selectionArgs, sortOrder);
            SQLiteDatabase db = eventSQLHelper.getReadableDatabase();
            Cursor cursor = queryAnnualEvents(query, db);
//            Cursor[] cursors = new Cursor[2];
//            cursors[1] = queryDynamicEvents(projection, selection, selectionArgs, sortOrder, db, builder);
//            SortCursor sortCursor = new SortCursor(cursors, AnnualEventsContract.DATE);
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

    private Cursor queryDynamicEvents(String[] projection, String selection, String[] selectionArgs, String sortOrder, SQLiteDatabase db) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(EventsDBContract.DynamicEventsContract.TABLE_NAME);
        return builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
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
