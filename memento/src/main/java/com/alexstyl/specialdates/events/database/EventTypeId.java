package com.alexstyl.specialdates.events.database;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.alexstyl.specialdates.events.database.EventTypeId.*;

@Retention(RetentionPolicy.SOURCE)
@IntDef({TYPE_BIRTHDAY, TYPE_NAMEDAY, TYPE_ANNIVERSARY, TYPE_OTHER, TYPE_CUSTOM})
public @interface EventTypeId {
    int TYPE_BIRTHDAY = 0;
    int TYPE_NAMEDAY = 1;
    int TYPE_ANNIVERSARY = 2;
    int TYPE_OTHER = 3;
    int TYPE_CUSTOM = 4;
}
