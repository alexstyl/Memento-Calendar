package com.alexstyl.specialdates.analytics;

public final class ActionWithParameters {

    private final Action actionName;
    private final String label;
    private final String value;

    public ActionWithParameters(Action actionName, String label, String value) {
        this.actionName = actionName;
        this.label = label;
        this.value = value;
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
