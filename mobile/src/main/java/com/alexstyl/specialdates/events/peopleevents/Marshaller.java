package com.alexstyl.specialdates.events.peopleevents;

import android.content.ContentValues;

interface Marshaller<T> {
    ContentValues[] marshall(T item);
}
