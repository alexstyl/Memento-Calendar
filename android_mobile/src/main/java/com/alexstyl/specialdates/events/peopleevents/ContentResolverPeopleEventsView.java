package com.alexstyl.specialdates.events.peopleevents;

import android.content.ContentResolver;
import android.net.Uri;

import com.alexstyl.specialdates.PeopleEventsView;
import com.alexstyl.specialdates.events.database.PeopleEventsContract;

public class ContentResolverPeopleEventsView implements PeopleEventsView {

    private static final Uri CONTENT_URI = PeopleEventsContract.PeopleEvents.CONTENT_URI;

    private final ContentResolver resolver;

    public ContentResolverPeopleEventsView(ContentResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void requestUpdate() {
        resolver.notifyChange(CONTENT_URI, null, false);
    }
}
