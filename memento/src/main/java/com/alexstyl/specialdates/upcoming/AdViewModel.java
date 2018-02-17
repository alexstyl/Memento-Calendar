package com.alexstyl.specialdates.upcoming;

import com.alexstyl.specialdates.date.Date;

final class AdViewModel implements UpcomingRowViewModel {

    private final Date afterDate;

    AdViewModel(Date afterDate) {
        this.afterDate = afterDate;
    }

    @Override
    public int getViewType() {
        return UpcomingRowViewType.AD;
    }

    @Override
    public long getId() {
        return afterDate.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AdViewModel that = (AdViewModel) o;

        return afterDate.equals(that.afterDate);

    }

    @Override
    public int hashCode() {
        return afterDate.hashCode();
    }
}
