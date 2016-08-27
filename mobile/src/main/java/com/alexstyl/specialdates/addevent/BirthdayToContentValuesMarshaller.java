package com.alexstyl.specialdates.addevent;

import android.content.ContentValues;

import com.alexstyl.specialdates.Marshaller;
import com.alexstyl.specialdates.contact.Birthday;

public class BirthdayToContentValuesMarshaller implements Marshaller<Birthday>{
    @Override
    public ContentValues[] marshall(Birthday item) {
        return new ContentValues[0];
    }
}
