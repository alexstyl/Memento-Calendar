package com.alexstyl.specialdates.events.peopleevents;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.alexstyl.specialdates.ErrorTracker;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.contact.ContactSource;
import com.alexstyl.specialdates.events.database.DatabaseContract.AnnualEventsContract;
import com.alexstyl.specialdates.events.database.EventTypeId;

import org.jetbrains.annotations.NotNull;

import static com.alexstyl.specialdates.contact.ContactSource.SOURCE_DEVICE;
import static com.alexstyl.specialdates.events.database.EventTypeId.TYPE_NAMEDAY;

public final class PeopleEventsPersister {

    private final SQLiteOpenHelper helper;

    public PeopleEventsPersister(SQLiteOpenHelper helper) {
        this.helper = helper;
    }

    public void deleteAllNamedays() {
        deleteAllEventsOfType(TYPE_NAMEDAY);
    }

    private void deleteAllEventsOfType(@EventTypeId int eventType) {
        SQLiteDatabase database = helper.getWritableDatabase();
        database.delete(AnnualEventsContract.TABLE_NAME, AnnualEventsContract.EVENT_TYPE + "==" + eventType, null);
    }

    public void deleteAllEventsOfSource(@ContactSource int source) {
        SQLiteDatabase database = helper.getWritableDatabase();
        database.delete(AnnualEventsContract.TABLE_NAME, AnnualEventsContract.SOURCE + "==" + source, null);
    }

    void deleteAllDeviceEvents() {
        SQLiteDatabase database = helper.getWritableDatabase();
        database.delete(AnnualEventsContract.TABLE_NAME, AnnualEventsContract.SOURCE + " == " + SOURCE_DEVICE, null);
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

    public void markContactAsVisible(Contact contact) {
        SQLiteDatabase database = helper.getWritableDatabase();

        ContentValues values = new ContentValues(1);
        values.put(AnnualEventsContract.VISIBLE, 1);

        database.update(AnnualEventsContract.TABLE_NAME,
                values,
                AnnualEventsContract.CONTACT_ID + " = " + contact.getContactID() +
                        " AND " + AnnualEventsContract.SOURCE + " = " + contact.getSource(),
                null);
    }

    public void markContactAsHidden(Contact contact) {
        SQLiteDatabase database = helper.getWritableDatabase();

        ContentValues values = new ContentValues(1);
        values.put(AnnualEventsContract.VISIBLE, 0);

        database.update(AnnualEventsContract.TABLE_NAME,
                values,
                AnnualEventsContract.CONTACT_ID + " = " + contact.getContactID() +
                        " AND " + AnnualEventsContract.SOURCE + " = " + contact.getSource(),
                null);
    }

    public boolean getVisibilityFor(@NotNull Contact contact) {
        SQLiteDatabase database = helper.getWritableDatabase();
        // TODO just count events the contact has
        Cursor query = database.query(AnnualEventsContract.TABLE_NAME,
                null,
                AnnualEventsContract.CONTACT_ID + " == " + contact.getContactID() +
                        " AND " + AnnualEventsContract.SOURCE + " == " + contact.getSource() +
                        " AND " + AnnualEventsContract.VISIBLE + " = 1",
                null,
                null,
                null,
                null
        );
        int count = query.getCount();
        query.close();
        return count > 0;
    }
}
