package com.alexstyl.specialdates.widgetprovider.upcomingevents;

import android.widget.RemoteViews;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.upcoming.MonthHeaderViewModel;

public class MonthBinder implements UpcomingEventViewBinder<MonthHeaderViewModel> {

    private final RemoteViews remoteViews;

    public MonthBinder(RemoteViews remoteViews) {
        this.remoteViews = remoteViews;
    }

    @Override
    public void bind(MonthHeaderViewModel viewModel) {
        remoteViews.setTextViewText(R.id.row_widget_text, viewModel.getMonthLabel());
    }

    @Override
    public RemoteViews getViews() {
        return remoteViews;
    }
}
