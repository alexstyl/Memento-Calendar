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

    @Override
    public long getId() {
        return monthLabel.hashCode();
    }

    public String getMonthLabel() {
        return monthLabel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MonthHeaderViewModel that = (MonthHeaderViewModel) o;

        return monthLabel.equals(that.monthLabel);

    }

    @Override
    public int hashCode() {
        return monthLabel.hashCode();
    }
}
