package com.alexstyl.specialdates.events.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns._ID
import com.alexstyl.specialdates.events.database.ContactColumns.CONTACT_ID
import com.alexstyl.specialdates.events.database.ContactColumns.DISPLAY_NAME
import com.alexstyl.specialdates.events.database.DatabaseContract.AnnualEventsContract.TABLE_NAME
import com.alexstyl.specialdates.events.database.EventColumns.DATE
import com.alexstyl.specialdates.events.database.EventColumns.DEVICE_EVENT_ID
import com.alexstyl.specialdates.events.database.EventColumns.EVENT_TYPE
import com.alexstyl.specialdates.events.database.EventColumns.SOURCE
import com.alexstyl.specialdates.events.database.EventColumns.VISIBLE

class EventSQLiteOpenHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS dynamic_events;")
        db.execSQL("DROP TABLE IF EXISTS annual_events;")
        onCreate(db)
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(("CREATE TABLE $TABLE_NAME ("
                + "$_ID INTEGER NOT NULL, "
                + "$DISPLAY_NAME TEXT NOT NULL, "
                + "$DEVICE_EVENT_ID INTEGER NOT NULL, "
                + "$CONTACT_ID INTEGER NOT NULL, "
                + "$DATE TEXT NOT NULL, "
                + "$EVENT_TYPE INTEGER NOT NULL, "
                + "$SOURCE INTEGER NOT NULL, "
                + "$VISIBLE INTEGER NOT NULL, "
                + "PRIMARY KEY ($_ID)"
                + ");"))
    }

    companion object {

        const val DATABASE_VERSION = 5

        private const val DATABASE_NAME = "events.db"

    }
}
