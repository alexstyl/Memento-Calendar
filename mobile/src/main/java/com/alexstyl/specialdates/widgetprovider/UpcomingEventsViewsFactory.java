package com.alexstyl.specialdates.widgetprovider;

import android.annotation.SuppressLint;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.TimePeriod;
import com.alexstyl.specialdates.upcoming.UpcomingRowViewModel;
import com.alexstyl.specialdates.upcoming.UpcomingRowViewType;
import com.alexstyl.specialdates.widgetprovider.upcomingevents.MonthBinder;
import com.alexstyl.specialdates.widgetprovider.upcomingevents.UpcomingEventViewBinder;
import com.alexstyl.specialdates.widgetprovider.upcomingevents.UpcomingEventsBinder;
import com.alexstyl.specialdates.widgetprovider.upcomingevents.UpcomingEventsProvider;

import java.util.List;

class UpcomingEventsViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final int VIEW_TYPE_COUNT = 3;
    private final String packageName;
    private final UpcomingEventsProvider peopleEventsProvider;

    private List<UpcomingRowViewModel> rows;
    private final WidgetImageLoader imageLoader;

    UpcomingEventsViewsFactory(String packageName, UpcomingEventsProvider peopleEventsProvider, WidgetImageLoader imageLoader) {
        this.packageName = packageName;
        this.peopleEventsProvider = peopleEventsProvider;
        this.imageLoader = imageLoader;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        UpcomingRowViewModel viewModel = rows.get(position);
        UpcomingEventViewBinder binder = createBinderFor(viewModel);
        binder.bind(viewModel);
        return binder.getViews();
    }

    @SuppressLint("SwitchIntDef")
    private UpcomingEventViewBinder createBinderFor(UpcomingRowViewModel viewModel) {
        switch (viewModel.getViewType()) {
            case UpcomingRowViewType.MONTH: {
                RemoteViews view = new RemoteViews(packageName, R.layout.row_widget_upcoming_event);
                return new MonthBinder(view);
            }
            case UpcomingRowViewType.YEAR: {
                RemoteViews view = new RemoteViews(packageName, R.layout.row_widget_upcoming_event_year);
                return new YearBinder(view);
            }
            case UpcomingRowViewType.UPCOMING_EVENTS: {
                return UpcomingEventsBinder.buildFor(packageName, imageLoader);
            }
            default:
                throw new IllegalStateException("Unhandled type " + viewModel.getViewType());
        }
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
