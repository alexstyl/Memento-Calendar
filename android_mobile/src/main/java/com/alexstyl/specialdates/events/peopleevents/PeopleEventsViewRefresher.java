package com.alexstyl.specialdates.events.peopleevents;

import com.alexstyl.specialdates.PeopleEventsView;

import java.util.Set;

public final class PeopleEventsViewRefresher {

    private final Set<PeopleEventsView> views;

    PeopleEventsViewRefresher(Set<PeopleEventsView> views) {
        this.views = views;
    }

    public void updateAllViews() {
        for (PeopleEventsView view : views) {
            view.requestUpdate();
        }
    }
}
