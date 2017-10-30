package com.alexstyl.specialdates.search;

import android.content.Context;

import com.alexstyl.specialdates.ui.loader.SimpleAsyncTaskLoader;

import java.util.List;

class SearchLoader extends SimpleAsyncTaskLoader<SearchResults> {

    private final String searchQuery;
    private final int searchCounter;

    private final PeopleEventsSearch peopleEventsSearch;
    private final ContactEventViewModelFactory viewModelFactory;

    SearchLoader(Context context,
                 PeopleEventsSearch peopleEventsSearch,
                 String query,
                 int searchCounter,
                 ContactEventViewModelFactory viewModelFactory) {
        super(context);
        this.searchQuery = query;
        this.searchCounter = searchCounter;
        this.peopleEventsSearch = peopleEventsSearch;
        this.viewModelFactory = viewModelFactory;
    }

    @Override
    public SearchResults loadInBackground() {
        List<ContactWithEvents> contactEvents = peopleEventsSearch.searchForContacts(searchQuery, searchCounter);
        List<ContactEventViewModel> viewModels = viewModelFactory.createViewModelFrom(contactEvents);
        boolean canLoadMore = viewModels.size() > searchCounter;

        return new SearchResults(searchQuery, viewModels, canLoadMore);
    }
}
