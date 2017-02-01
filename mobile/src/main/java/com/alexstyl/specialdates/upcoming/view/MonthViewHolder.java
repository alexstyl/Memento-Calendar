package com.alexstyl.specialdates.upcoming.view;

import android.view.View;
import android.widget.TextView;

import com.alexstyl.specialdates.upcoming.MonthHeaderViewModel;

final class MonthViewHolder extends UpcomingRowViewHolder<MonthHeaderViewModel> {

    private final TextView monthView;

    MonthViewHolder(View convertView, TextView monthView) {
        super(convertView);
        this.monthView = monthView;
    }

    @Override
    public void bind(MonthHeaderViewModel element, OnUpcomingEventClickedListener listener) {
        // ignore the listener. The row is not clickable
        monthView.setText(element.getMonthLabel());
    }
}
