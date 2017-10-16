package com.alexstyl.specialdates.events.peopleevents;

import com.alexstyl.specialdates.PeopleEventsView;

import java.util.List;

public final class PeopleEventsViewRefresher {

    private final List<PeopleEventsView> views;

    PeopleEventsViewRefresher(List<PeopleEventsView> views) {
        this.views = views;
    }

    public void updateAllViews() {
        for (PeopleEventsView view : views) {
            view.requestUpdate();
        }
    }
}
