package com.alexstyl.specialdates.upcoming;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alexstyl.specialdates.upcoming.ui.UpcomingEventsAdapter;

abstract public class MonthSectionScrollListener extends RecyclerView.OnScrollListener {

    private final UpcomingEventsAdapter adapter;
    private final LinearLayoutManager layoutManager;

    public MonthSectionScrollListener(UpcomingEventsAdapter adapter, LinearLayoutManager layoutManager) {
        this.adapter = adapter;
        this.layoutManager = layoutManager;
    }

    private int currentMonth;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (isDisplayingNoEvents()) {
            return;
        }
        int top = layoutManager.findFirstVisibleItemPosition();
        int month = adapter.getMonthAt(top);
        if (month != currentMonth) {
            onDifferentMonthScrolled(month);
            currentMonth = month;
        }
    }

    protected abstract void onDifferentMonthScrolled(int month);

    private boolean isDisplayingNoEvents() {
        return adapter.getItemCount() == 0;
    }

}
