package com.alexstyl.specialdates.events.peopleevents;

import android.content.ContentValues;

import com.alexstyl.specialdates.contact.Birthday;

import java.util.List;

public class BirthdayMarshaller implements Marshaller<Birthday> {
    @Override
    public ContentValues[] marshall(List<Birthday> item) {
        return new ContentValues[0];
    }
}
