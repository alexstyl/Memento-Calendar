package com.alexstyl.specialdates.events.peopleevents;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.alexstyl.specialdates.ErrorTracker;
import com.alexstyl.specialdates.events.database.EventSQLiteOpenHelper;
import com.alexstyl.specialdates.events.database.EventsDBContract.AnnualEventsContract;
import com.alexstyl.specialdates.events.database.EventsDBContract.DynamicEventsContract;

public class PeopleEventsPersister {

    private final EventSQLiteOpenHelper helper;

    public PeopleEventsPersister(EventSQLiteOpenHelper helper) {
        this.helper = helper;
    }

    public void deleteAllNamedays() {
        deleteAllEventsOfType(AnnualEventsContract.TYPE_NAMEDAY);
    }

    public void deleteAllBirthdays() {
        deleteAllEventsOfType(AnnualEventsContract.TYPE_BIRTHDAY);
    }

    private void deleteAllEventsOfType(int type) {
        SQLiteDatabase database = helper.getWritableDatabase();
        database.delete(AnnualEventsContract.TABLE_NAME, AnnualEventsContract.EVENT_TYPE + "=" + type, null);
        database.delete(DynamicEventsContract.TABLE_NAME, DynamicEventsContract.EVENT_TYPE + "=" + type, null);
    }

    public void insertDynamicEvents(ContentValues[] values) {
        insertEventsInTable(values, DynamicEventsContract.TABLE_NAME);
    }

    public void insertAnnualEvents(ContentValues[] values) {
        insertEventsInTable(values, AnnualEventsContract.TABLE_NAME);
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
