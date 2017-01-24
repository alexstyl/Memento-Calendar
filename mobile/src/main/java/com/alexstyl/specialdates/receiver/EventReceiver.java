package com.alexstyl.specialdates.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.alexstyl.specialdates.ErrorTracker;
import com.alexstyl.specialdates.wear.WearSyncService;
import com.alexstyl.specialdates.widgetprovider.TodayWidgetProvider;

public class EventReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_LOCALE_CHANGED.equals(action)) {
            pushLocaleChanged(context);
        }
    }

    private void pushLocaleChanged(Context context) {
        ErrorTracker.updateLocaleUsed();
        TodayWidgetProvider.updateWidgets(context);
        WearSyncService.startService(context);
    }

}
