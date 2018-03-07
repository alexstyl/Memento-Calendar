package com.alexstyl.specialdates.wear;

import android.content.Context;
import android.content.Intent;

import com.alexstyl.specialdates.UpcomingEventsView;

public class WearSyncUpcomingEventsView implements UpcomingEventsView {

    private final Context context;

    public WearSyncUpcomingEventsView(Context context) {
        this.context = context;
    }

    @Override
    public void reloadUpcomingEventsView() {
        Intent service = new Intent(context, WearSyncService.class);
        context.startService(service);
    }
}
