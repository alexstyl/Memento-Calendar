package com.alexstyl.specialdates.upcoming;

public final class YearHeaderViewModel implements UpcomingRowViewModel {
    private final String year;

    YearHeaderViewModel(String year) {
        this.year = year;
    }

    @Override
    public int getViewType() {
        return UpcomingRowViewType.YEAR;
    }

    public String getYear() {
        return year;
    }
}
