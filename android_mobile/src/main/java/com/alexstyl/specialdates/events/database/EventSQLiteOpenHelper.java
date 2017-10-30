package com.alexstyl.specialdates.events.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.alexstyl.specialdates.events.database.DatabaseContract.AnnualEventsContract;
import com.alexstyl.specialdates.events.peopleevents.EventPreferences;

public class EventSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA_SEP = ", ";
    private static final String NOT_NULL = " NOT NULL ";
    private static final String DATABASE_NAME = "events.db";
    private static final int DATABASE_VERSION = 4;

    private final EventPreferences eventPreferences;

    public EventSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        eventPreferences = new EventPreferences(context);
    }

    private static final String SQL_CREATE_ANNUAL_EVENTS =
            "CREATE TABLE " + AnnualEventsContract.TABLE_NAME + " ("
                    + AnnualEventsContract._ID + INT_TYPE + NOT_NULL + COMMA_SEP
                    + AnnualEventsContract.DISPLAY_NAME + TEXT_TYPE + NOT_NULL + COMMA_SEP
                    + AnnualEventsContract.DEVICE_EVENT_ID + INT_TYPE + NOT_NULL + COMMA_SEP
                    + AnnualEventsContract.CONTACT_ID + INT_TYPE + NOT_NULL + COMMA_SEP
                    + AnnualEventsContract.DATE + TEXT_TYPE + NOT_NULL + COMMA_SEP
                    + AnnualEventsContract.EVENT_TYPE + INT_TYPE + NOT_NULL + COMMA_SEP
                    + AnnualEventsContract.SOURCE + INT_TYPE + NOT_NULL + COMMA_SEP
                    + " PRIMARY KEY (" + AnnualEventsContract._ID + ")"
                    + ")";

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS dynamic_events;");
        db.execSQL("DROP TABLE IF EXISTS annual_events;");
        eventPreferences.reset();
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ANNUAL_EVENTS);
    }
}
