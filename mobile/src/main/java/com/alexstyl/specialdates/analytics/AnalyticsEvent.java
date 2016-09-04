package com.alexstyl.specialdates.analytics;

import android.os.Bundle;

public class AnalyticsEvent {

    private final Bundle bundle = new Bundle();
    private final Events eventName;
    private boolean enabled;

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
        this.bundle.putString(key, value);
        return this;
    }

    public AnalyticsEvent withData(String key, boolean value) {
        this.bundle.putBoolean(key, value);
        return this;
    }

    public AnalyticsEvent asSuccess(boolean value) {
        return withData("success", value);
    }

    public AnalyticsEvent reason(String reason) {
        return withData("reason", reason);
    }

    public AnalyticsEvent setEnabled(boolean enabled) {
        return withData("enabled", enabled);
    }

    public enum Events {
        ADD_BIRTHDAY("add birthday"),
        DAILY_REMINDER("daily reminder"),
        DONATION("donation");

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
