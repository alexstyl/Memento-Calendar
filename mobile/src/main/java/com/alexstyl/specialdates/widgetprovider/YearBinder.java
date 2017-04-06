package com.alexstyl.specialdates.widgetprovider;

import android.widget.RemoteViews;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.upcoming.YearHeaderViewModel;
import com.alexstyl.specialdates.widgetprovider.upcomingevents.UpcomingEventViewBinder;

class YearBinder implements UpcomingEventViewBinder<YearHeaderViewModel> {

    private final RemoteViews views;

    YearBinder(RemoteViews views) {
        this.views = views;
    }

    @Override
    public void bind(YearHeaderViewModel viewModel) {
        views.setTextViewText(R.id.row_widget_text, viewModel.getYear());
    }

    @Override
    public RemoteViews getViews() {
        return views;
    }
}
