package com.alexstyl.specialdates.upcoming.widget.list;

import android.widget.RemoteViews;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.upcoming.YearHeaderViewModel;

class YearBinder implements UpcomingEventViewBinder<YearHeaderViewModel> {

    private final RemoteViews views;

    YearBinder(RemoteViews views) {
        this.views = views;
    }

    @Override
    public void bind(YearHeaderViewModel viewModel) {
        views.setTextViewText(R.id.widget_row_upcoming_year, viewModel.getYear());
    }

    @Override
    public RemoteViews getViews() {
        return views;
    }
}
