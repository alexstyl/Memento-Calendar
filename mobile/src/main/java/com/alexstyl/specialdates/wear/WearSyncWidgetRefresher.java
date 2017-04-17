package com.alexstyl.specialdates.wear;

import android.content.Context;
import android.content.Intent;

public class WearSyncWidgetRefresher implements WidgetRefresher {

    private final Context context;

    public WearSyncWidgetRefresher(Context context) {
        this.context = context;
    }

    @Override
    public void refreshView() {
        Intent service = new Intent(context, WearSyncService.class);
        context.startService(service);
    }
}
