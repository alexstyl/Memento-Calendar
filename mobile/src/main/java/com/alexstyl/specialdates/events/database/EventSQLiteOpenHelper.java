package com.alexstyl.specialdates.events.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.alexstyl.specialdates.events.database.EventsDBContract.AnnualEvents;
import com.alexstyl.specialdates.events.database.EventsDBContract.DynamicEvents;

public class EventSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "events.db";
    private static final int INITIAL_CODE = 1;
    private static final int DATABASE_VERSION = INITIAL_CODE;

    public EventSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA_SEP = ", ";

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    private static String NOT_NULL = " NOT NULL ";

    private static final String SQL_CREATE_ANNUAL_EVENTS =
            "CREATE TABLE " + AnnualEvents.TABLE_NAME + " ("
                    + AnnualEvents._ID + INT_TYPE + NOT_NULL + COMMA_SEP
                    + AnnualEvents.DISPLAY_NAME + TEXT_TYPE + NOT_NULL + COMMA_SEP
                    + AnnualEvents.CONTACT_ID + INT_TYPE + NOT_NULL + COMMA_SEP
                    + AnnualEvents.DATE + TEXT_TYPE + NOT_NULL + COMMA_SEP
                    + AnnualEvents.SOURCE + INT_TYPE + NOT_NULL + COMMA_SEP
                    + AnnualEvents.EVENT_TYPE + INT_TYPE + NOT_NULL + COMMA_SEP
                    + " PRIMARY KEY (" + AnnualEvents._ID + ")"
                    + ")";

    private static final String SQL_CREATE_DYNAMIC_EVENTS =
            "CREATE TABLE " + DynamicEvents.TABLE_NAME + " ("
                    + DynamicEvents._ID + INT_TYPE + NOT_NULL + COMMA_SEP
                    + DynamicEvents.CONTACT_ID + INT_TYPE + NOT_NULL + COMMA_SEP
                    + DynamicEvents.DISPLAY_NAME + TEXT_TYPE + NOT_NULL + COMMA_SEP
                    + DynamicEvents.DATE + TEXT_TYPE + NOT_NULL + COMMA_SEP
                    + DynamicEvents.SOURCE + INT_TYPE + NOT_NULL + COMMA_SEP
                    + DynamicEvents.EVENT_TYPE + INT_TYPE + NOT_NULL + COMMA_SEP
                    + " PRIMARY KEY (" + DynamicEvents._ID + ")"
                    + ")";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ANNUAL_EVENTS);
        db.execSQL(SQL_CREATE_DYNAMIC_EVENTS);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
