package com.alexstyl.specialdates.addevent.ui;

import android.widget.Filter;

import com.alexstyl.specialdates.contact.Contact;

import java.util.ArrayList;
import java.util.List;

abstract class DeviceContactsFilter extends Filter {

    private static final int LOAD_A_SINGLE_CONTACT = 1;
    private final ContactsSearch contactsSearch;

    DeviceContactsFilter(ContactsSearch contactsSearch) {
        this.contactsSearch = contactsSearch;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        if (constraint == null) {
            return emptyResults();
        }
        List<Contact> contacts = contactsSearch.searchForContacts(constraint.toString(), LOAD_A_SINGLE_CONTACT);

        FilterResults filterResults = new FilterResults();
        filterResults.values = contacts;
        filterResults.count = contacts.size();
        return filterResults;
    }



    private FilterResults emptyResults() {
        FilterResults filterResults = new FilterResults();
        filterResults.values = new ArrayList<>();
        filterResults.count = 0;
        return filterResults;
    }

    @Override
    protected final void publishResults(CharSequence constraint, FilterResults results) {
        onContactsFiltered((List<Contact>) results.values);
    }

    abstract void onContactsFiltered(List<Contact> contacts);
}
