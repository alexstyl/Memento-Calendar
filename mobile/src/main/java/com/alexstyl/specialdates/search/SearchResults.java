package com.alexstyl.specialdates.search;

import java.util.List;

class SearchResults {

    private final String searchQuery;
    private final List<ContactWithEvents> contacts;
    private final boolean canLoadMore;

    SearchResults(String searchQuery, List<ContactWithEvents> contacts, boolean canLoadMore) {
        this.searchQuery = searchQuery;
        this.contacts = contacts;
        this.canLoadMore = canLoadMore;
    }

    boolean canLoadMore() {
        return canLoadMore;
    }

    public List<ContactWithEvents> getContacts() {
        return contacts;
    }

    String getSearchQuery() {
        return searchQuery;
    }
}
