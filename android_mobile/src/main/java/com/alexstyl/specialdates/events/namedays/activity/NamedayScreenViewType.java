package com.alexstyl.specialdates.events.namedays.activity;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.alexstyl.specialdates.events.namedays.activity.NamedayScreenViewType.CONTACT;
import static com.alexstyl.specialdates.events.namedays.activity.NamedayScreenViewType.NAMEDAY;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
        NAMEDAY,
        CONTACT
})
@interface NamedayScreenViewType {
    int NAMEDAY = 0;
    int CONTACT = 1;
}
