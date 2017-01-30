package com.alexstyl.specialdates.upcoming;

public final class MonthHeaderViewModel implements UpcomingRowViewModel {

    private final String monthLabel;

    public MonthHeaderViewModel(String monthLabel) {
        this.monthLabel = monthLabel;
    }

    @Override
    public int getViewType() {
        return UpcomingRowViewType.MONTH_HEADER;
    }

    public String getMonthLabel() {
        return monthLabel;
    }
}
