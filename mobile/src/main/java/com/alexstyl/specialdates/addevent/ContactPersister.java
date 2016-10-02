package com.alexstyl.specialdates.addevent;

import android.content.ContentResolver;
import android.content.Context;

import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.date.Date;

class ContactPersister {

    private final ContentResolver contentResolver;
    private final Context context;

    public ContactPersister(Context context, ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
        this.context = context;
    }

    public void createContactWithNameAndBirthday(String contactName, Date birthday, AccountData account) {
        new ContactWithBirthdayCreateTask(contactName, birthday, contentResolver, context, account).execute();
    }

    public void addBirthdayToExistingContact(Date birthday, Contact contact) {
        new AddBirthdayToContactTask(context, contentResolver, birthday, contact).execute();
    }
}
