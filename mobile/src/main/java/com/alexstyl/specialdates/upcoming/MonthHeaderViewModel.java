package com.alexstyl.specialdates.upcoming;

public final class MonthHeaderViewModel implements UpcomingRowViewModel {

    private final String monthLabel;

    MonthHeaderViewModel(String monthLabel) {
        this.monthLabel = monthLabel;
    }

    @Override
    public int getViewType() {
        return UpcomingRowViewType.MONTH;
    }

    public String getMonthLabel() {
        return monthLabel;
    }
}
