package com.alexstyl.specialdates.events.database;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.CLASS)
@IntDef({EventColumns.TYPE_BIRTHDAY, EventColumns.TYPE_NAMEDAY, EventColumns.TYPE_ANNIVERSARY, EventColumns.TYPE_OTHER, EventColumns.TYPE_CUSTOM})
public @interface EventTypeId {
}
