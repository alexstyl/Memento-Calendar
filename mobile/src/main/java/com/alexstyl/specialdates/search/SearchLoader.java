package com.alexstyl.specialdates.search;

import android.content.Context;
import android.os.Handler;

import com.alexstyl.specialdates.ui.loader.SimpleAsyncTaskLoader;
import com.alexstyl.specialdates.util.ContactsObserver;

import java.util.List;

class SearchLoader extends SimpleAsyncTaskLoader<SearchResults> {

    private final String searchQuery;
    private final int searchCounter;

    private final ContactsObserver observer;
    private final PeopleEventsSearch peopleEventsSearch;

    SearchLoader(Context context, PeopleEventsSearch peopleEventsSearch, String query, int searchCounter) {
        super(context);
        this.searchQuery = query;
        this.searchCounter = searchCounter;
        this.peopleEventsSearch = peopleEventsSearch;

        observer = new ContactsObserver(context.getContentResolver(), new Handler());
        observer.registerWith(new ContactsObserver.Callback() {
            @Override
            public void onContactsUpdated() {
                onContentChanged();
            }
        });
    }

    @Override
    protected void onUnregisterObserver() {
        super.onUnregisterObserver();
        observer.unregister();
    }

    @Override
    public SearchResults loadInBackground() {
        List<ContactWithEvents> contacts = peopleEventsSearch.searchForContacts(searchQuery, searchCounter);
        boolean canLoadMore = contacts.size() > searchCounter;
        return new SearchResults(searchQuery, contacts, canLoadMore);
    }
}
