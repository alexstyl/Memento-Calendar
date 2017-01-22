package com.alexstyl.specialdates.analytics;

import com.alexstyl.specialdates.events.peopleevents.EventType;

public interface Analytics {
    void trackAction(Action action);

    void trackAction(ActionWithParameters event);

    void trackScreen(Screen screen);

    void trackAddEventsCancelled();

    void trackEventAddedSuccessfully();

    void trackContactSelected();

    void trackEventDatePicked(EventType eventType);

    void trackEventRemoved(EventType eventType);

    void trackImageCaptured();

    void trackExistingImagePicked();

    void trackAvatarSelected();

    void trackContactUpdated();

    void trackContactCreated();
}
