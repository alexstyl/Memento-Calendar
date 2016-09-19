package com.alexstyl.specialdates.events;

import android.content.ContentValues;

interface Marshaller<T> {
    ContentValues[] marshall(T item);
}
