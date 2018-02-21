package com.alexstyl.specialdates.people;

import com.alexstyl.specialdates.contact.Contact;

public interface PeopleViewHolderListener {
    void onPersonClicked(Contact contact);

    void onFacebookImport();

    void onAddContactClicked();
}
