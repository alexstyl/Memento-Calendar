package com.alexstyl.specialdates.search;

import android.content.Context;
import android.os.Handler;

import com.alexstyl.specialdates.contact.Contact;
import com.alexstyl.specialdates.ui.loader.SimpleAsyncTaskLoader;
import com.alexstyl.specialdates.util.ContactsObserver;

import java.util.List;

public class SearchLoader extends SimpleAsyncTaskLoader<SearchResults> {

    private final ContactWithEventsSearch contactWithEventsSearch;
    private final String searchQuery;
    private final int searchCounter;

    private final ContactsObserver observer;

    public SearchLoader(Context context, String query, int mSearchCounter) {
        super(context);
        this.contactWithEventsSearch = ContactWithEventsSearch.newInstance(context);
        this.searchQuery = query;
        this.searchCounter = mSearchCounter;

        observer = new ContactsObserver(context.getContentResolver(), new Handler());
        observer.registerWith(new ContactsObserver.Callback() {
            @Override
            public void onContactsUpdated() {
                onContentChanged();
            }
        });
    }

    @Override
    protected void onAbandon() {
        super.onAbandon();
        observer.unregister();
    }

    @Override
    public SearchResults loadInBackground() {
        List<Contact> contacts = contactWithEventsSearch.searchForContacts(searchQuery, searchCounter);

        boolean canLoadMore = contacts.size() > searchCounter;
        return new SearchResults(searchQuery, contacts, canLoadMore);
    }
}
