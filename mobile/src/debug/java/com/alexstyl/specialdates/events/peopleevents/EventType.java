package com.alexstyl.specialdates.events.peopleevents;

import android.content.res.Resources;
import android.support.annotation.ColorRes;

import com.alexstyl.specialdates.events.database.EventTypeId;

public interface EventType {

    String getEventName(Resources resources);

    @ColorRes
    int getColorRes();

    @EventTypeId
    int getId();
}
