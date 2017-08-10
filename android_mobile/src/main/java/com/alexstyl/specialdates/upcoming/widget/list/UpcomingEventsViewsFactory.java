package com.alexstyl.specialdates.upcoming.widget.list;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.alexstyl.resources.DimensionResources;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.TimePeriod;
import com.alexstyl.specialdates.upcoming.UpcomingEventsProvider;
import com.alexstyl.specialdates.upcoming.UpcomingRowViewModel;
import com.alexstyl.specialdates.upcoming.UpcomingRowViewType;

import java.util.List;

class UpcomingEventsViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final int VIEW_TYPE_COUNT = 3;
    private static final int DAYS_IN_A_MONTH = 30;
    private final String packageName;
    private final UpcomingEventsProvider peopleEventsProvider;
    private final DimensionResources dimensResources;
    private final Context context;
    private final CircularAvatarFactory avatarFactory;

    private List<UpcomingRowViewModel> rows;

    UpcomingEventsViewsFactory(String packageName,
                               UpcomingEventsProvider peopleEventsProvider,
                               DimensionResources dimensResources,
                               Context context,
                               CircularAvatarFactory avatarFactory) {
        this.packageName = packageName;
        this.peopleEventsProvider = peopleEventsProvider;
        this.dimensResources = dimensResources;
        this.context = context;
        this.avatarFactory = avatarFactory;
    }

    @Override
    public void onCreate() {
        // do nothing
    }

    @Override
    public void onDataSetChanged() {
        Date date = Date.Companion.today();
        rows = peopleEventsProvider.calculateEventsBetween(aMonthFrom(date));
    }

    private TimePeriod aMonthFrom(Date date) {
        return TimePeriod.Companion.between(date, date.addDay(DAYS_IN_A_MONTH));
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
            case UpcomingRowViewType.YEAR:
                RemoteViews yearView = new RemoteViews(packageName, R.layout.row_widget_upcoming_event_year);
                return new YearBinder(yearView);
            case UpcomingRowViewType.MONTH:
                RemoteViews monthView = new RemoteViews(packageName, R.layout.row_widget_upcoming_event_month);
                return new MonthBinder(monthView);
            case UpcomingRowViewType.UPCOMING_EVENTS:
                RemoteViews remoteViews = new RemoteViews(packageName, R.layout.row_widget_upcoming_event);
                return new UpcomingEventsBinder(remoteViews, context, avatarFactory, dimensResources);
            default:
                throw new IllegalStateException("Unhandled type " + viewModel.getViewType());
        }
    }

    @Override
    public RemoteViews getLoadingView() {
        return null; // use the default loading view by returning null
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
    public void onDestroy() {
        // do nothing
    }
}
