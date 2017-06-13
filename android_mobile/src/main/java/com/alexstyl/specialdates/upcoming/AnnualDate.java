package com.alexstyl.specialdates.upcoming;

import com.alexstyl.specialdates.date.Date;

final class AnnualDate {
    private final Date date;

    AnnualDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AnnualDate that = (AnnualDate) o;

        return date.getMonth() == that.date.getMonth() &&
                date.getDayOfMonth() == that.date.getDayOfMonth();
    }

    @Override
    public int hashCode() {
        int result = date.getDayOfMonth();
        result = 31 * result + date.getMonth();
        return result;
    }
}
