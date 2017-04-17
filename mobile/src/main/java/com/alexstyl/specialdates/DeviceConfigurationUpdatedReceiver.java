package com.alexstyl.specialdates;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * A {@linkplain BroadcastReceiver} that keeps track whether the user has updated some option on their device external to Memento which can affect the app.
 */
public class DeviceConfigurationUpdatedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ExternalWidgetRefresher externalWidgetRefresher = ExternalWidgetRefresher.get(context.getApplicationContext());

        String action = intent.getAction();
        if (Intent.ACTION_LOCALE_CHANGED.equals(action)) {
            ErrorTracker.updateLocaleUsed();
            externalWidgetRefresher.refreshAllWidgets();
        } else if (Intent.ACTION_DATE_CHANGED.equals(action)) {
            externalWidgetRefresher.refreshAllWidgets();
        }
    }

}
