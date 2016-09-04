package com.alexstyl.specialdates.analytics;

import android.os.Bundle;

public class AnalyticsEvent {

    private final Bundle bundle = new Bundle();
    private final Events eventName;

    public String getName() {
        return eventName.name;
    }

    public Bundle data() {
        return bundle;
    }

    public AnalyticsEvent(Events eventName) {
        this.eventName = eventName;
    }

    public AnalyticsEvent withData(String key, String value) {
        this.bundle.remove(key);
        this.bundle.putString(key, value);
        return this;
    }

    public AnalyticsEvent withData(String key, boolean value) {
        this.bundle.remove(key);
        this.bundle.putBoolean(key, value);
        return this;
    }

    public enum Events {
        ADD_BIRTHDAY("add birthday"),
        DAILY_REMINDER("daily reminder");

        private final String name;

        Events(String name) {
            this.name = name;
        }

    }

    @Override
    public String toString() {
        return "AnalyticsEvent{" +
                "eventName=" + eventName +
                ", bundle=" + bundle +
                '}';
    }
}
