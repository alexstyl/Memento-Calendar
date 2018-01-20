package com.alexstyl.specialdates.person;

import android.content.Intent;
import android.graphics.drawable.Drawable;

public final class IntentOptionViewModel {

    private final Drawable icon;
    private final String label;
    private final Intent intent;

    IntentOptionViewModel(Drawable icon, String label, Intent intent) {
        this.icon = icon;
        this.label = label;
        this.intent = intent;
    }

    Drawable getActivityIcon() {
        return icon;
    }

    String getLabel() {
        return label;
    }

    Intent getIntent() {
        return intent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        IntentOptionViewModel that = (IntentOptionViewModel) o;

        if (!icon.equals(that.icon)) {
            return false;
        }
        if (!label.equals(that.label)) {
            return false;
        }
        return intent.equals(that.intent);

    }

    @Override
    public int hashCode() {
        int result = icon.hashCode();
        result = 31 * result + label.hashCode();
        result = 31 * result + intent.hashCode();
        return result;
    }
}

