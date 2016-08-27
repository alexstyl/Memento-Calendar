package com.alexstyl.specialdates.util;

import android.content.ActivityNotFoundException;
import android.content.Context;

/**
 * <p>Created by Alex on 9/5/2014.</p>
 */
public interface IntentAction {

     void onStartAction(Context context) throws ActivityNotFoundException;
}
