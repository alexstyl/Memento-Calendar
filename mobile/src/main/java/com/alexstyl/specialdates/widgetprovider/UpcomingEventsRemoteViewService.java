package com.alexstyl.specialdates.widgetprovider;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class UpcomingEventsRemoteViewService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new UpcomingEventsViewsFactory(getPackageName());
    }
}
