package com.alexstyl.specialdates.search;

import java.util.List;

final class SearchResults {

    private final String searchQuery;
    private final List<ContactEventViewModel> contacts;
    private final boolean canLoadMore;

    SearchResults(String searchQuery, List<ContactEventViewModel> contacts, boolean canLoadMore) {
        this.searchQuery = searchQuery;
        this.contacts = contacts;
        this.canLoadMore = canLoadMore;
    }

    boolean canLoadMore() {
        return canLoadMore;
    }

    List<ContactEventViewModel> getViewModels() {
        return contacts;
    }

    String getSearchQuery() {
        return searchQuery;
    }
}
