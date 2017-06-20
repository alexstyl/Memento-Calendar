package com.alexstyl.specialdates.events.peopleevents;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;

import com.alexstyl.specialdates.events.database.PeopleEventsContract;

public class PeopleEventsObserver extends ContentObserver {

    private static final Uri URI = PeopleEventsContract.PeopleEvents.CONTENT_URI;
    private final ContentResolver resolver;

    private OnPeopleEventsChanged callback;

    public PeopleEventsObserver(ContentResolver resolver) {
        super(new Handler());
        this.resolver = resolver;
    }

    public void startObserving(OnPeopleEventsChanged callback) {
        this.callback = callback;
        resolver.registerContentObserver(URI, false, this);
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        callback.onPeopleEventsUpdated();
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        callback.onPeopleEventsUpdated();
    }

    public void stopObserving() {
        resolver.unregisterContentObserver(this);
    }

    public interface OnPeopleEventsChanged {
        void onPeopleEventsUpdated();
    }
}
