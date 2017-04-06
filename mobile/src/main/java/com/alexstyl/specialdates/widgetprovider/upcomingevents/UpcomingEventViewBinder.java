package com.alexstyl.specialdates.widgetprovider.upcomingevents;

import android.widget.RemoteViews;

public interface UpcomingEventViewBinder<T> {

    void bind(T viewModel);

    RemoteViews getViews();
}
