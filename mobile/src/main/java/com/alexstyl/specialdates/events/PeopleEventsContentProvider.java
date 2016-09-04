package com.alexstyl.specialdates.events;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.alexstyl.specialdates.date.DateParseException;
import com.alexstyl.specialdates.date.DayDate;
import com.alexstyl.specialdates.events.PeopleEventsContract.PeopleEvents;
import com.alexstyl.specialdates.events.database.EventSQLiteOpenHelper;
import com.alexstyl.specialdates.events.database.EventsDBContract;
import com.alexstyl.specialdates.upcoming.LoadingTimeDuration;
import com.alexstyl.specialdates.util.DateParser;
import com.novoda.notils.exception.DeveloperError;

public class PeopleEventsContentProvider extends ContentProvider {

    private static final int PEOPLE_EVENTS = 10;

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
        uriMatcher.addURI(PeopleEventsContract.AUTHORITY, PeopleEventsContract.PeopleEvents.PATH, PEOPLE_EVENTS);
    }

    private SQLArgumentBuilder sqlArgumentBuilder = new SQLArgumentBuilder();

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        refreshEventsIfNeeded();

        SQLiteDatabase db = eventSQLHelper.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        int uriType = uriMatcher.match(uri);

        if (uriType == PEOPLE_EVENTS) {
            if (isInvalidSetOfArguments(selectionArgs)) {
                throwUnsupportedOperation("3+ select args");
            }
            Cursor[] cursors = new Cursor[2];

            cursors[0] = queryAnnualEvents(projection, selection, selectionArgs, sortOrder, db, builder);
            cursors[1] = queryDynamicEvents(projection, selection, selectionArgs, sortOrder, db, builder);

            SortCursor sortCursor = new SortCursor(cursors, EventColumns.DATE);
            sortCursor.setNotificationUri(getContext().getContentResolver(), uri);
            return sortCursor;
        } else {
            throwForInvalidUri("querying", uri);
            return null;
        }
    }

    private boolean isInvalidSetOfArguments(String[] selectionArgs) {
        return selectionArgs != null && selectionArgs.length > 2;
    }

    private Cursor queryDynamicEvents(String[] projection, String selection, String[] selectionArgs, String sortOrder, SQLiteDatabase db, SQLiteQueryBuilder builder) {
        builder.setTables(EventsDBContract.DynamicEvents.TABLE_NAME);
        return builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
    }

    private Cursor queryAnnualEvents(String[] projection, String selection, String[] selectionArgs, String sortOrder, SQLiteDatabase db, SQLiteQueryBuilder builder) {
        builder.setTables(EventsDBContract.AnnualEvents.TABLE_NAME);

        if (selectionArgs != null) {
            LoadingTimeDuration duration = getDurationfrom(selection, selectionArgs);
            selection = getSelectionArgumentsForAnnualEvents(duration);
            projection = makeAnnualProjectFrom(projection, duration.getFrom().getYear());
        }
        return builder.query(db, projection, selection, null, null, null, sortOrder);
    }

    private String[] makeAnnualProjectFrom(String[] projection, int year) {
        if (projection == null || projection.length == 0) {
            return projection;
        }
        String[] newProjection = new String[projection.length];
        for (int i = 0; i < newProjection.length; i++) {
            if (EventColumns.DATE.equals(projection[i])) {
                newProjection[i] = sqlArgumentBuilder.dateIn(year);
            } else {
                newProjection[i] = projection[i];
            }
        }
        return newProjection;
    }

    private LoadingTimeDuration getDurationfrom(String selection, String[] selectionArgs) {
        DateParser parser = new DateParser();
        if (selectionArgs.length == 1) {
            try {
                DayDate date = parser.parse(selectionArgs[0]);
                if (selection.contains(">") || selection.contains("<")) {
                    DayDate endOfYear = DayDate.endOfYear(date.getYear());
                    return new LoadingTimeDuration(date, endOfYear);
                } else {
                    return new LoadingTimeDuration(date, date);
                }
            } catch (DateParseException e) {
                throw new DeveloperError(e.getMessage());
            }
        }
        if (selectionArgs.length == 2) {
            DayDate[] dates = new DayDate[2];
            for (int i = 0; i < selectionArgs.length; i++) {
                try {
                    dates[i] = parser.parse(selectionArgs[i]);
                } catch (DateParseException e) {
                    throw new DeveloperError(e.getMessage());
                }
            }
            return new LoadingTimeDuration(dates[0], dates[1]);
        }
        throw new DeveloperError("Invalid length " + selectionArgs);
    }

    private String getSelectionArgumentsForAnnualEvents(LoadingTimeDuration duration) {
        return sqlArgumentBuilder.dateBetween(duration);
    }

    private void throwForInvalidUri(String when, Uri uri) {
        throw new DeveloperError("Invalid uri passed " + uri + " while " + when);
    }

    private void refreshEventsIfNeeded() {
        peopleEventsUpdater.updateEventsIfNeeded();
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case PEOPLE_EVENTS:
                return PeopleEvents.CONTENT_TYPE;
            default:
                return null;
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throwUnsupportedOperation("insert");
        return null;
    }

    private void throwUnsupportedOperation(String insert) {
        throw new DeveloperError(insert + " is not supported by the " + getClass().getSimpleName());
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throwUnsupportedOperation("delete");
        return -1;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throwUnsupportedOperation("update");
        return -1;
    }

}
