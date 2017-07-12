package com.alexstyl.specialdates.search;

import android.content.Context;

import com.alexstyl.specialdates.events.peopleevents.PeopleEventsObserver;
import com.alexstyl.specialdates.ui.loader.SimpleAsyncTaskLoader;

import java.util.List;

class SearchLoader extends SimpleAsyncTaskLoader<SearchResults> {

    private final String searchQuery;
    private final int searchCounter;

    private final PeopleEventsObserver observer;
    private final PeopleEventsSearch peopleEventsSearch;
    private final ContactEventViewModelFactory viewModelFactory;

    SearchLoader(Context context,
                 PeopleEventsSearch peopleEventsSearch,
                 PeopleEventsObserver observer,
                 String query,
                 int searchCounter,
                 ContactEventViewModelFactory viewModelFactory
    ) {
        super(context);
        this.observer = observer;
        this.searchQuery = query;
        this.searchCounter = searchCounter;
        this.peopleEventsSearch = peopleEventsSearch;
        this.viewModelFactory = viewModelFactory;

        this.observer.startObserving(new PeopleEventsObserver.OnPeopleEventsChanged() {
            @Override
            public void onPeopleEventsUpdated() {
                onContentChanged();
            }
        });
    }

    @Override
    protected void onUnregisterObserver() {
        super.onUnregisterObserver();
        observer.stopObserving();
    }

    @Override
    public SearchResults loadInBackground() {
        List<ContactWithEvents> contactEvents = peopleEventsSearch.searchForContacts(searchQuery, searchCounter);
        List<ContactEventViewModel> viewModels = viewModelFactory.createViewModelFrom(contactEvents);
        boolean canLoadMore = viewModels.size() > searchCounter;

        return new SearchResults(searchQuery, viewModels, canLoadMore);
    }
}
