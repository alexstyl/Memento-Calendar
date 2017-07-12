package com.alexstyl.specialdates.wear;

import android.content.Context;
import android.content.Intent;

import com.alexstyl.specialdates.PeopleEventsView;

public class WearSyncPeopleEventsView implements PeopleEventsView {

    private final Context context;

    public WearSyncPeopleEventsView(Context context) {
        this.context = context;
    }

    @Override
    public void requestUpdate() {
        Intent service = new Intent(context, WearSyncService.class);
        context.startService(service);
    }
}
