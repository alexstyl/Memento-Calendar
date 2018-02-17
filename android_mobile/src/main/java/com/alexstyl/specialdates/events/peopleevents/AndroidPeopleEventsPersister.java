package com.alexstyl.specialdates.events.peopleevents;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.alexstyl.specialdates.CrashAndErrorTracker;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.contact.ContactSource;
import com.alexstyl.specialdates.date.ContactEvent;
import com.alexstyl.specialdates.events.database.DatabaseContract.AnnualEventsContract;
import com.alexstyl.specialdates.events.database.EventTypeId;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import static com.alexstyl.specialdates.contact.ContactSource.SOURCE_DEVICE;
import static com.alexstyl.specialdates.events.database.EventTypeId.TYPE_NAMEDAY;

public final class AndroidPeopleEventsPersister implements PeopleEventsPersister {

    private final SQLiteOpenHelper helper;
    private final ContactEventsMarshaller marshaller;
    private final CrashAndErrorTracker tracker;

    public AndroidPeopleEventsPersister(SQLiteOpenHelper helper, ContactEventsMarshaller marshaller, CrashAndErrorTracker tracker) {
        this.helper = helper;
        this.marshaller = marshaller;
        this.tracker = tracker;
    }

    @Override
    public void deleteAllNamedays() {
        deleteAllEventsOfType(TYPE_NAMEDAY);
    }

    private void deleteAllEventsOfType(@EventTypeId int eventType) {
        SQLiteDatabase database = helper.getWritableDatabase();
        database.delete(AnnualEventsContract.TABLE_NAME, AnnualEventsContract.EVENT_TYPE + "==" + eventType, null);
    }

    @Override
    public void deleteAllEventsOfSource(@ContactSource int source) {
        SQLiteDatabase database = helper.getWritableDatabase();
        database.delete(AnnualEventsContract.TABLE_NAME, AnnualEventsContract.SOURCE + "==" + source, null);
    }

    @Override
    public void deleteAllDeviceEvents() {
        SQLiteDatabase database = helper.getWritableDatabase();
        database.delete(AnnualEventsContract.TABLE_NAME, AnnualEventsContract.SOURCE + " == " + SOURCE_DEVICE, null);
    }

    @Override
    public void insertAnnualEvents(List<ContactEvent> events) {
        ContentValues[] contentValues = marshaller.marshall(events);
        insertEventsInTable(contentValues, AnnualEventsContract.TABLE_NAME);
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
            tracker.track(ex);
        } finally {
            database.endTransaction();
        }
    }

    @Override
    public void markContactAsVisible(Contact contact) {
        SQLiteDatabase database = helper.getWritableDatabase();

        ContentValues values = new ContentValues(1);
        values.put(AnnualEventsContract.VISIBLE, 1);

        database.update(
                AnnualEventsContract.TABLE_NAME,
                values,
                AnnualEventsContract.CONTACT_ID + " = " + contact.getContactID()
                        + " AND " + AnnualEventsContract.SOURCE + " = " + contact.getSource(),
                null
        );
    }

    @Override
    public void markContactAsHidden(Contact contact) {
        SQLiteDatabase database = helper.getWritableDatabase();

        ContentValues values = new ContentValues(1);
        values.put(AnnualEventsContract.VISIBLE, 0);

        database.update(
                AnnualEventsContract.TABLE_NAME,
                values,
                AnnualEventsContract.CONTACT_ID + " = " + contact.getContactID()
                        + " AND " + AnnualEventsContract.SOURCE + " = " + contact.getSource(),
                null
        );
    }

    @Override
    public boolean getVisibilityFor(@NotNull Contact contact) {
        SQLiteDatabase database = helper.getWritableDatabase();
        // TODO just COUNT() events the contact has
        Cursor query = database.query(
                AnnualEventsContract.TABLE_NAME,
                null,
                AnnualEventsContract.CONTACT_ID + " == " + contact.getContactID()
                        + " AND " + AnnualEventsContract.SOURCE + " == " + contact.getSource()
                        + " AND " + AnnualEventsContract.VISIBLE + " = 1",
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
