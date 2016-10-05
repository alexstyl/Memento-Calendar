package com.alexstyl.specialdates.events.peopleevents;

import android.content.ContentValues;

import java.util.List;

interface Marshaller<T> {
    ContentValues[] marshall(List<T> item);
}
