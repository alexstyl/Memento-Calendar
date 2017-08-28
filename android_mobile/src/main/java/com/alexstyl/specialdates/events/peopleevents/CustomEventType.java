package com.alexstyl.specialdates.events.peopleevents;

import android.provider.ContactsContract.CommonDataKinds.Event;

import com.alexstyl.resources.Strings;
import com.alexstyl.specialdates.R;

import static com.alexstyl.specialdates.events.database.EventTypeId.TYPE_CUSTOM;

public class CustomEventType implements EventType {

    private final String name;

    public CustomEventType(String name) {
        this.name = name;
    }

    @Override
    public String getEventName(Strings strings) {
        return name;
    }

    @Override
    public int getColorRes() {
        return R.color.purple_custom_event;
    }

    @Override
    public int getId() {
        return TYPE_CUSTOM;
    }

    @Override
    public int getIconResId() {
        return R.drawable.ic_custom;
    }

    @Override
    public int getAndroidType() {
        return Event.TYPE_CUSTOM;
    }
}
