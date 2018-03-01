package com.alexstyl.specialdates.wear;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.alexstyl.android.Version;
import com.alexstyl.specialdates.PeopleEventsView;

public class WearSyncPeopleEventsView implements PeopleEventsView {

    private final Context context;

    public WearSyncPeopleEventsView(Context context) {
        this.context = context;
    }

    @TargetApi(Build.VERSION_CODES.O)
    @Override
    public void refreshEventsView() {
        Intent service = new Intent(context, WearSyncService.class);
        if (Version.hasOreo()) {
            context.startForegroundService(service);
        } else {
            context.startService(service);
        }
    }
}
