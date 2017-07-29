package com.alexstyl.specialdates.analytics;

public enum Widget {
    UPCOMING_EVENTS_SCROLLING("upcoming_events_scrolling"),
    UPCOMING_EVENTS_SIMPLE("upcoming_events_simple");

    private final String widgetName;

    Widget(String widgetName) {
        this.widgetName = widgetName;
    }

    public String getWidgetName() {
        return widgetName;
    }
}
