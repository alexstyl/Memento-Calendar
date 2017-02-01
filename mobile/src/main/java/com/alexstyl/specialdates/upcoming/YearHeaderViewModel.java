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

    @Override
    public long getId() {
        return hashCode();
    }

    public String getYear() {
        return year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        YearHeaderViewModel that = (YearHeaderViewModel) o;

        return year.equals(that.year);

    }

    @Override
    public int hashCode() {
        return year.hashCode();
    }
}
