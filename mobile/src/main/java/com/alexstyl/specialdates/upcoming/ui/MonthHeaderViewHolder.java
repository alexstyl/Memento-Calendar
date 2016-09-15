package com.alexstyl.specialdates.upcoming.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.upcoming.MonthLabels;
import com.alexstyl.specialdates.upcoming.MonthOfYear;

public class MonthHeaderViewHolder extends RecyclerView.ViewHolder {
    private final MonthLabels monthLabels;
    private final TextView header;

    public static MonthHeaderViewHolder createFor(ViewGroup parent, MonthLabels monthLabels) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_header_month, parent, false);
        return new MonthHeaderViewHolder(view, monthLabels);
    }

    public MonthHeaderViewHolder(View convertView, MonthLabels monthLabels) {
        super(convertView);
        this.monthLabels = monthLabels;
        this.header = (TextView) convertView.findViewById(android.R.id.text1);
    }

    public void displayMonth(MonthOfYear monthOfYear, int currentYear) {
        if (monthOfYear.getYear() == currentYear) {
            header.setText(monthLabels.getMonthOfYear(monthOfYear.getMonth()));
        } else {
            header.setText(String.valueOf(monthOfYear.getYear()));
        }
    }
}
