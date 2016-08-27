package com.alexstyl.specialdates.contact;

import android.support.v4.util.LruCache;

class ContactCache<T extends Contact> {

    private final LruCache<Long, T> cache;

    ContactCache(int maxSize) {
        this.cache = new LruCache<>(maxSize);
    }

    void addContact(T contact) {
        cache.put(keyFor(contact), contact);
    }

    T getContact(Long id) {
        return cache.get(id);
    }

    private Long keyFor(T contact) {
        return contact.getContactID();
    }

    public int size() {
        return cache.size();
    }

    public void evictAll() {
        cache.evictAll();
    }

    public boolean containsContactWith(long contactID) {
        return cache.get(contactID) != null;
    }
}
