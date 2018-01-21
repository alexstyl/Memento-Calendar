package com.alexstyl.specialdates.wear;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.alexstyl.android.Version;
import com.alexstyl.specialdates.PeopleEventsView;

public class WearSyncPeopleEventsView implements PeopleEventsView {

    private final Context context;

    public WearSyncPeopleEventsView(Context context) {
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onEventsUpdated() {
        Intent service = new Intent(context, WearSyncService.class);
        if (Version.hasOreo()) {
            context.startForegroundService(service);
        } else {
            context.startService(service);
        }
    }
}
