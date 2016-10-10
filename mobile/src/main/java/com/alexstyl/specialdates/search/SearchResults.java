package com.alexstyl.specialdates.search;

import com.alexstyl.specialdates.contact.Contact;

import java.util.ArrayList;
import java.util.List;

class SearchResults {

    private final String searchQuery;
    private final List<Contact> contacts;
    private final boolean canLoadMore;

    SearchResults(String searchQuery, List<Contact> contacts, boolean canLoadMore) {
        this.searchQuery = searchQuery;
        this.contacts = contacts;
        this.canLoadMore = canLoadMore;
    }

    boolean canLoadMore() {
        return canLoadMore;
    }

    public List<Contact> getContacts() {
        return new ArrayList<>(contacts);
    }

    String getSearchQuery() {
        return searchQuery;
    }
}
