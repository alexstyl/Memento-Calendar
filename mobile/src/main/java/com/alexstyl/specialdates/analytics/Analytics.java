package com.alexstyl.specialdates.analytics;

public interface Analytics {
    void trackAction(AnalyticsAction event);

    void trackScreen(Screen screen);
}
