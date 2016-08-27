package com.alexstyl.specialdates.addevent;

import android.content.ContentResolver;
import android.content.Context;

import com.alexstyl.specialdates.contact.Birthday;
import com.alexstyl.specialdates.contact.Contact;

class ContactPersister {

    private final ContentResolver contentResolver;
    private final Context context;

    public ContactPersister(Context context, ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
        this.context = context;
    }

    public void createContactWithNameAndBirthday(String contactName, Birthday birthday, AccountData account) {
        new ContactWithBirthdayCreateTask(contactName, birthday, contentResolver, context, account).execute();
    }

    public void addBirthdayToExistingContact(Birthday birthday, Contact contact) {
        new AddBirthdayToContactTask(context, contentResolver, birthday, contact).execute();
    }
}
