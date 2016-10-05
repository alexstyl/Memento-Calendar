package com.alexstyl.specialdates.addevent;

import android.content.ContentResolver;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.date.Date;

public class AddBirthdayToContactTask extends AsyncTask<Void, Void, Boolean> {

    private final Context context;
    private final ContentResolver contentResolver;
    private final Date birthday;
    private final Contact contact;

    public AddBirthdayToContactTask(Context context, ContentResolver contentResolver, Date birthday, Contact contact) {
        this.context = context;
        this.contentResolver = contentResolver;
        this.birthday = birthday;
        this.contact = contact;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        BirthdayEntry birthdayEntry = new BirthdayQuery(contentResolver)
                .forContact(contact)
                .queryContactsBirthday();

        if (birthdayEntry != null) {
            return updateContactBirthday(birthdayEntry);
        } else {
            return newBirthdayForContact();
        }

    }

    private Boolean updateContactBirthday(BirthdayEntry oldBirthday) {
        return new BirthdayQuery(contentResolver).forContact(contact).replaceBirthdays(oldBirthday, birthday);
    }

    private boolean newBirthdayForContact() {
        return new BirthdayQuery(contentResolver).forContact(contact).insertBirthday(birthday);
    }

    @Override
    protected void onPostExecute(Boolean succeeded) {
        if (succeeded) {
            Toast.makeText(context, R.string.add_birthday_contact_updated, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, R.string.add_birthday_failed_to_update_contact, Toast.LENGTH_SHORT).show();
        }
    }

}
