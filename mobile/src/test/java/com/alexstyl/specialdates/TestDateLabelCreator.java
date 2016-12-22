package com.alexstyl.specialdates;

import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.search.DateLabelCreator;
import com.alexstyl.specialdates.upcoming.MonthLabels;

import java.util.Locale;

final public class TestDateLabelCreator implements DateLabelCreator {

    private final MonthLabels monthLabels;

    public static DateLabelCreator forUS() {
        MonthLabels monthLabels = MonthLabels.forLocale(Locale.US);
        return new TestDateLabelCreator(monthLabels);
    }

    private TestDateLabelCreator(MonthLabels monthLabels) {
        this.monthLabels = monthLabels;
    }

    @Override
    public String createLabelWithoutYearFor(Date date) {
        return monthLabels.getMonthOfYear(date.getMonth()) + " " + date.getDayOfMonth();
    }

    @Override
    public String createLabelWithYearFor(Date date) {
        return monthLabels.getMonthOfYear(date.getMonth()) + " " + date.getDayOfMonth() + " " + date.getYear();
    }
}
