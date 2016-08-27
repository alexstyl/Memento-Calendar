package com.alexstyl.specialdates.search;

import com.alexstyl.specialdates.contact.Contact;

import java.util.ArrayList;
import java.util.List;

public class SearchResults {

    private final String searchQuery;
    private final List<Contact> contacts;
    private final boolean canLoadMore;

    public SearchResults(String searchQuery, List<Contact> contacts, boolean canLoadMore) {
        this.searchQuery = searchQuery;
        this.contacts = contacts;
        this.canLoadMore = canLoadMore;
    }

    public boolean canLoadMore() {
        return canLoadMore;
    }

    public List<Contact> getContacts() {
        return new ArrayList<>(contacts);
    }

    public String getSearchQuery() {
        return searchQuery;
    }
}
