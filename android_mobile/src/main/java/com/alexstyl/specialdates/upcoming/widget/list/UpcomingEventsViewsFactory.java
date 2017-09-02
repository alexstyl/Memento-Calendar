package com.alexstyl.specialdates.upcoming.widget.list;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

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
    private final Resources resources;
    private final CircularAvatarFactory avatarFactory;

    private List<UpcomingRowViewModel> rows;

    UpcomingEventsViewsFactory(String packageName,
                               UpcomingEventsProvider peopleEventsProvider,
                               Resources resources,
                               CircularAvatarFactory avatarFactory) {
        this.packageName = packageName;
        this.resources = resources;
        this.peopleEventsProvider = peopleEventsProvider;
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
            case UpcomingRowViewType.DATE_HEADER: {
                RemoteViews view = new RemoteViews(packageName, R.layout.widget_upcoming_events_list_date);
                return new DateHeaderBinder(view);
            }
            case UpcomingRowViewType.BANKHOLIDAY: {
                RemoteViews view = new RemoteViews(packageName, R.layout.widget_upcomingevents_list_bankholiday);
                return new BankHolidayBinder(view);
            }
            case UpcomingRowViewType.NAMEDAY_CARD: {
                RemoteViews view = new RemoteViews(packageName, R.layout.widget_upcoming_events_list_nameday);
                return new NamedayCardBinder(view);
            }
            case UpcomingRowViewType.CONTACT_EVENT: {
                RemoteViews remoteViews = new RemoteViews(packageName, R.layout.widget_upcoming_events_list_contact_event);
                return new ContactEventBinder(remoteViews, resources, avatarFactory);
            }
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
