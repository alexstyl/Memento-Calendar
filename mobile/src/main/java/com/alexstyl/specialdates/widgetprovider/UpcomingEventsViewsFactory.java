package com.alexstyl.specialdates.widgetprovider;

import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.TimePeriod;
import com.alexstyl.specialdates.upcoming.UpcomingRowViewModel;
import com.alexstyl.specialdates.widgetprovider.upcomingevents.RemoteViewBinder;
import com.alexstyl.specialdates.widgetprovider.upcomingevents.UpcomingEventsProvider;

import java.util.List;

class UpcomingEventsViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final int VIEW_TYPE_COUNT = 1;
    private final String packageName;
    private final UpcomingEventsProvider peopleEventsProvider;
    private final RemoteViewBinder binder;

    private List<UpcomingRowViewModel> rows;

    UpcomingEventsViewsFactory(String packageName, UpcomingEventsProvider peopleEventsProvider, RemoteViewBinder binder) {
        this.packageName = packageName;
        this.peopleEventsProvider = peopleEventsProvider;
        this.binder = binder;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        UpcomingRowViewModel contactEvent = rows.get(position);
        RemoteViews view = new RemoteViews(packageName, R.layout.row_widget_upcoming_event);
        binder.bind(view, contactEvent);
        return view;
    }

    @Override
    public RemoteViews getLoadingView() {
        // TODO add spinner
        return null;
    }

    @Override
    public int getCount() {
        return rows.size();
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onCreate() {
        Date date = Date.today();
        rows = peopleEventsProvider.calculateEventsBetween(TimePeriod.between(date, date.addDay(30)));
    }

    @Override
    public void onDataSetChanged() {
        // no-op
    }

    @Override
    public void onDestroy() {
        // no-op
    }
}
