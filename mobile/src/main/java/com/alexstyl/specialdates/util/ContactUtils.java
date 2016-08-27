package com.alexstyl.specialdates.util;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;

import com.alexstyl.specialdates.ErrorTracker;
import com.alexstyl.specialdates.entity.Email;
import com.alexstyl.specialdates.entity.Phone;

import java.util.ArrayList;
import java.util.List;

public class ContactUtils {

    private ContactUtils() {
    }

    private static class DataTypeQuery {
        // Stuff like projection and selection are going to be the same, since
        // Phone and Email contain the same columns
        static String[] PROJECTION = {
                ContactsContract.Data.DATA1, // NUMBER
                ContactsContract.Data.DATA2, // TYPE
                ContactsContract.Data.DATA3, // LABEL
        };
        static String SELECTION = ContactsContract.Data.CONTACT_ID + " = ?";

        static String SORTORDER = ContactsContract.Data.IS_PRIMARY + " DESC";
    }

    /**
     * Returns whether the contact with the particular contactID has at least one phone number
     *
     * @return
     */
    public static boolean hasPhoneNumber(ContentResolver resolver, long contactID) {

        Cursor phoneCursor;
        String[] selectionArgs = {
                String.valueOf(contactID)
        };

        Uri contentUri = CommonDataKinds.Phone.CONTENT_URI;

        phoneCursor = resolver.query(contentUri, DataTypeQuery.PROJECTION,
                                     DataTypeQuery.SELECTION,
                                     selectionArgs, DataTypeQuery.SORTORDER + " LIMIT 1"
        );

        if (phoneCursor == null) {
            return false;
        }

        try {
            return phoneCursor.getCount() > 0;
        } catch (Exception e) {
            ErrorTracker.track(e);
        } finally {
            if (!phoneCursor.isClosed()) {
                phoneCursor.close();
            }
        }

        return false;
    }

    public static List<Phone> getAllPhones(ContentResolver resolver, long contactID) {

        ArrayList<Phone> phones = new ArrayList<>();
        Cursor phoneCursor;
        String[] selectionArgs = {
                String.valueOf(contactID)
        };

        Uri contentUri = CommonDataKinds.Phone.CONTENT_URI;

        phoneCursor = resolver.query(contentUri, DataTypeQuery.PROJECTION,
                                     DataTypeQuery.SELECTION,
                                     selectionArgs, DataTypeQuery.SORTORDER
        );

        if (phoneCursor == null) {
            return phones;
        }

        try {
            int colData1 = phoneCursor.getColumnIndex(ContactsContract.Data.DATA1);
            int colData2 = phoneCursor.getColumnIndex(ContactsContract.Data.DATA2);
            int colData3 = phoneCursor.getColumnIndex(ContactsContract.Data.DATA3);

            while (phoneCursor.moveToNext()) {
                String number = phoneCursor.getString(colData1);
                Integer type = phoneCursor.getInt(colData2);
                String label = phoneCursor.getString(colData3);

                Phone data = new Phone(number, type, label);
                if (!phones.contains(data)) {
                    phones.add(data);
                }
            }

        } catch (Exception e) {
            ErrorTracker.track(e);
        } finally {
            if (phoneCursor != null && !phoneCursor.isClosed()) {
                phoneCursor.close();
            }
        }

        return phones;

    }

    public static boolean hasEmail(ContentResolver resolver, long contactID) {
        Cursor cur = null;
        String[] selectionArgs = {
                String.valueOf(contactID)
        };

        Uri contentUri = CommonDataKinds.Email.CONTENT_URI;

        cur = resolver.query(contentUri, DataTypeQuery.PROJECTION,
                             DataTypeQuery.SELECTION, selectionArgs, DataTypeQuery.SORTORDER + " LIMIT 1"
        );

        if (cur == null) {
            return false;
        }
        try {
            return cur.getCount() > 0;
        } catch (Exception e) {
            ErrorTracker.track(e);
        } finally {
            if (!cur.isClosed()) {
                cur.close();
            }
        }

        return false;
    }

    public static List<Email> getAllEMails(ContentResolver resolver, long contactID) {
        Cursor cur;
        String[] selectionArgs = {
                String.valueOf(contactID)
        };

        Uri contentUri = CommonDataKinds.Email.CONTENT_URI;

        cur = resolver.query(contentUri, DataTypeQuery.PROJECTION,
                             DataTypeQuery.SELECTION,
                             selectionArgs, DataTypeQuery.SORTORDER
        );
        List<Email> mails = new ArrayList<>();

        if (cur == null) {
            return mails;
        }

        try {
            int colData1 = cur.getColumnIndex(ContactsContract.Data.DATA1);
            int colData2 = cur.getColumnIndex(ContactsContract.Data.DATA2);
            int colData3 = cur.getColumnIndex(ContactsContract.Data.DATA3);

            while (cur.moveToNext()) {
                String number = cur.getString(colData1);
                Integer type = cur.getInt(colData2);
                String label = cur.getString(colData3);

                Email data = new Email(number, type, label);
                if (!mails.contains(data)) {
                    mails.add(data);
                }
            }

        } catch (Exception e) {
            ErrorTracker.track(e);
        } finally {
            if (!cur.isClosed()) {
                cur.close();
            }
        }

        return mails;
    }

}
