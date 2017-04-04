package com.alexstyl.specialdates.widgetprovider;

import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.alexstyl.specialdates.R;

import java.util.Arrays;
import java.util.List;

class UpcomingEventsViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final int VIEW_TYPE_COUNT = 1;
    private final String packageName;
    private final List<String> rows = Arrays.asList("1", "2", "3");

    UpcomingEventsViewsFactory(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews view = new RemoteViews(packageName, R.layout.row_widget_upcoming_event);
        view.setTextViewText(R.id.row_widget_text, rows.get(position));
        return view;
    }

    @Override
    public RemoteViews getLoadingView() {
        // TODO
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
        // no impl
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
