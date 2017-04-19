package com.alexstyl.specialdates.widgetprovider.upcomingevents;

import android.widget.RemoteViews;

import com.alexstyl.specialdates.upcoming.UpcomingRowViewModel;

public interface UpcomingEventViewBinder<T extends UpcomingRowViewModel> {

    void bind(T viewModel);

    RemoteViews getViews();
}
