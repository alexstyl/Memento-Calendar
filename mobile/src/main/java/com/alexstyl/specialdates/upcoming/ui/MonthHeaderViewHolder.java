package com.alexstyl.specialdates.upcoming.ui;

import android.view.View;
import android.widget.TextView;

import com.alexstyl.specialdates.upcoming.MonthHeaderViewModel;
import com.alexstyl.specialdates.upcoming.view.OnUpcomingEventClickedListener;

final class MonthHeaderViewHolder extends UpcomingRowViewHolder<MonthHeaderViewModel> {

    private final TextView monthView;

    MonthHeaderViewHolder(View convertView, TextView monthView) {
        super(convertView);
        this.monthView = monthView;
    }

    @Override
    public void bind(MonthHeaderViewModel element, OnUpcomingEventClickedListener listener) {
        // ignore the listener. The row is not clickable
        monthView.setText(element.getMonthLabel());
    }
}
