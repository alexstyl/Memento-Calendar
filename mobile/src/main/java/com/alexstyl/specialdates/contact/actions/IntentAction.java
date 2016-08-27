package com.alexstyl.specialdates.contact.actions;

import android.content.ActivityNotFoundException;
import android.content.Context;

public interface IntentAction {
    void onStartAction(Context context) throws ActivityNotFoundException;
}
