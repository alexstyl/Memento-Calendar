package com.alexstyl.specialdates.addevent.bottomsheet;

import android.content.Intent;
import android.graphics.drawable.Drawable;

final class IntentOptionViewModel {

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

}

