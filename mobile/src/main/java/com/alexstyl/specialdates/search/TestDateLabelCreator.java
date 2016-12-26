package com.alexstyl.specialdates.search;

import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.upcoming.MonthLabels;

import java.util.Locale;

final class TestDateLabelCreator implements DateLabelCreator {

    private final MonthLabels monthLabels;

    public static DateLabelCreator newInstance() {
        MonthLabels monthLabels = MonthLabels.forLocale(Locale.US);
        return new TestDateLabelCreator(monthLabels);
    }

    private TestDateLabelCreator(MonthLabels monthLabels) {
        this.monthLabels = monthLabels;
    }

    @Override
    public String createLabelFor(Date date) {
        return monthLabels.getMonthOfYear(date.getMonth()) + " " + date.getDayOfMonth();
    }
}
