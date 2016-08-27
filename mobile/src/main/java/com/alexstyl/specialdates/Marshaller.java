package com.alexstyl.specialdates;

import android.content.ContentValues;

public interface Marshaller<T> {
    ContentValues[] marshall(T item);
}
