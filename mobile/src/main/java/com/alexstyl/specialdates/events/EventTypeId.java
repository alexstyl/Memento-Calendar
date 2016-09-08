package com.alexstyl.specialdates.events;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({EventColumns.TYPE_BIRTHDAY, EventColumns.TYPE_NAMEDAY})
public @interface EventTypeId {
}
