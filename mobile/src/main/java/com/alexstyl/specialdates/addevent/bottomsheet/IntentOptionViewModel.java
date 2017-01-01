package com.alexstyl.specialdates.addevent.bottomsheet;

import android.content.Intent;
import android.graphics.drawable.Drawable;

final public class IntentOptionViewModel {

    private final Drawable icon;
    private final String label;
    private final Intent intent;

    IntentOptionViewModel(Drawable icon, String label, Intent intent) {
        this.icon = icon;
        this.label = label;
        this.intent = intent;
    }

    public Drawable getActivityIcon() {
        return icon;
    }

    public String getLabel() {
        return label;
    }

    public Intent getIntent() {
        return intent;
    }

}

