package com.alexstyl.specialdates.analytics;

public final class AnalyticsAction {

    private final Action actionName;
    private final String label;
    private final String value;

    public AnalyticsAction(Action actionName, String label, String value) {
        this.actionName = actionName;
        this.label = label;
        this.value = value;
    }

    public AnalyticsAction(Action action, String label, boolean value) {
        this.actionName = action;
        this.label = label;
        this.value = value ? "true" : "false";
    }

    public String getName() {
        return actionName.getName();
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }

}
