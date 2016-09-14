package com.alexstyl.specialdates.analytics;

public interface Analytics {
    void trackAction(Action goToToday);

    void trackAction(ActionWithParameters event);

    void trackScreen(Screen screen);
}
