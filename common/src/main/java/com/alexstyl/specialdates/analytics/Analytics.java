package com.alexstyl.specialdates.analytics;

public interface Analytics {
    void trackAction(Action action);

    void trackAction(ActionWithParameters event);

    void trackScreen(Screen screen);
}
