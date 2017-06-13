package com.alexstyl.specialdates.upcoming.widget.list;

import android.widget.RemoteViews;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.upcoming.MonthHeaderViewModel;

import static android.view.View.GONE;

public class MonthBinder implements UpcomingEventViewBinder<MonthHeaderViewModel> {

    private final RemoteViews remoteViews;

    public MonthBinder(RemoteViews remoteViews) {
        this.remoteViews = remoteViews;
    }

    @Override
    public void bind(MonthHeaderViewModel viewModel) {
        remoteViews.setViewVisibility(R.id.widget_row_upcoming_month, GONE);
    }

    @Override
    public RemoteViews getViews() {
        return remoteViews;
    }
}
