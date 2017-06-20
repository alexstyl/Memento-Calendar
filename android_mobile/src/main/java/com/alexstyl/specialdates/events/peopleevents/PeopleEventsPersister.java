package com.alexstyl.specialdates.events.peopleevents;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.alexstyl.specialdates.ErrorTracker;
import com.alexstyl.specialdates.events.database.DatabaseContract.AnnualEventsContract;
import com.alexstyl.specialdates.events.database.EventSQLiteOpenHelper;
import com.alexstyl.specialdates.events.database.EventTypeId;
import com.alexstyl.specialdates.events.database.PeopleEventsContract;
import com.alexstyl.specialdates.events.database.SourceType;

public final class PeopleEventsPersister {

    private static final Uri CONTENT_URI = PeopleEventsContract.PeopleEvents.CONTENT_URI;

    private final EventSQLiteOpenHelper helper;
    private final ContentResolver resolver;

    public PeopleEventsPersister(ContentResolver resolver, EventSQLiteOpenHelper helper) {
        this.resolver = resolver;
        this.helper = helper;
    }

    public void deleteAllNamedays() {
        deleteAllEventsOfType(AnnualEventsContract.TYPE_NAMEDAY);
    }

    private void deleteAllEventsOfType(@EventTypeId int eventType) {
        SQLiteDatabase database = helper.getWritableDatabase();
        database.delete(AnnualEventsContract.TABLE_NAME, AnnualEventsContract.EVENT_TYPE + "==" + eventType, null);
        resolver.notifyChange(CONTENT_URI, null);
    }

    public void deleteAllEventsOfSource(@SourceType int source) {
        SQLiteDatabase database = helper.getWritableDatabase();
        database.delete(AnnualEventsContract.TABLE_NAME, AnnualEventsContract.SOURCE + "==" + source, null);
        resolver.notifyChange(CONTENT_URI, null);
    }

    void deleteAllDeviceEvents() {
        SQLiteDatabase database = helper.getWritableDatabase();
        database.delete(AnnualEventsContract.TABLE_NAME, AnnualEventsContract.SOURCE + " == " + AnnualEventsContract.SOURCE_DEVICE, null);
        resolver.notifyChange(CONTENT_URI, null);
    }

    public void insertAnnualEvents(ContentValues[] values) {
        insertEventsInTable(values, AnnualEventsContract.TABLE_NAME);
        resolver.notifyChange(CONTENT_URI, null);
    }

    private void insertEventsInTable(ContentValues[] values, String tableName) {
        if (values.length == 0) {
            return;
        }
        SQLiteDatabase database = helper.getWritableDatabase();
        try {
            database.beginTransaction();
            for (ContentValues value : values) {
                database.insert(tableName, null, value);
            }
            database.setTransactionSuccessful();
        } catch (SQLException ex) {
            ErrorTracker.track(ex);
        } finally {
            database.endTransaction();
        }
    }
}
