package com.alexstyl.specialdates.upcoming.view;

import android.view.View;
import android.widget.TextView;

import com.alexstyl.specialdates.upcoming.YearHeaderViewModel;

final class YearViewHolder extends UpcomingRowViewHolder<YearHeaderViewModel> {

    private final TextView yearView;

    YearViewHolder(View convertView, TextView yearView) {
        super(convertView);
        this.yearView = yearView;
    }

    @Override
    public void bind(YearHeaderViewModel element, OnUpcomingEventClickedListener listener) {
        // ignore the listener. The row is not clickable
        yearView.setText(element.getYear());
    }
}
