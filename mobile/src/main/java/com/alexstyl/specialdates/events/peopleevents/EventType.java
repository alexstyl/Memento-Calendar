package com.alexstyl.specialdates.events.peopleevents;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;

import com.alexstyl.resources.StringResources;
import com.alexstyl.specialdates.events.database.EventTypeId;

public interface EventType {

    String getEventName(StringResources stringResources);

    @ColorRes
    int getColorRes();

    @EventTypeId
    int getId();

    @DrawableRes
    int getIconResId();

    int getAndroidType();
}
