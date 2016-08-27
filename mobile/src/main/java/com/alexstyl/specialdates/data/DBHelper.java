package com.alexstyl.specialdates.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final boolean DEBUG = false;
    private static final String TAG = DBHelper.class.getSimpleName();
    /**
     * Initial
     */
    private static final int DB_CODE_INIT = 1;

    private static final int DB_CODE_NAMEDAY_FIX = 3;
    private static final int DB_CODE_FACEBOOK_FRIENDS = 4;

    /**
     * Namedays is created from file
     */
    private static final int DB_CODE_EXTERNAL_DB = 5;

    private static final int DB_CODE_FB_NEW = 6;

    public static final int DATABASE_VERSION = DB_CODE_FB_NEW;
    public static final String DATABASE_NAME = "Namedays.db";

    private static final String NULLABLE = null;
    // --- SQL Queries ---

    private static final String TEXT_TYPE = " TEXT";
    private static final String TEXT_INT = " INTEGER";
    private static final String COMMA_SEP = ", ";

    public static final String SQL_DELETE_NAMEDAYS = "DROP TABLE IF EXISTS namedays";
    public static final String SQL_DELETE_FB_FRIENDS = "DROP TABLE IF EXISTS fb_friends";

    // --- SQL Queries

    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    static private DBHelper sInstance;

    public static DBHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DBHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    public SQLiteDatabase getDatabase() {
        synchronized (sInstance) {
            return this.getWritableDatabase();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_DELETE_NAMEDAYS);
        db.execSQL(SQL_DELETE_FB_FRIENDS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            onActualUpgrade(db, oldVersion, newVersion);
        } else {
            onActualDowngrade(db, oldVersion, newVersion);
        }
    }

    private void onActualDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    private void onActualUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
