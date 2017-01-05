package com.alexstyl.specialdates.upcoming;

import com.alexstyl.specialdates.date.MonthInt;

import java.text.DateFormatSymbols;
import java.util.Arrays;
import java.util.Locale;

public class MonthLabels {

    private final String[] monthLabels;

    public static MonthLabels forLocale(Locale locale) {
        DateFormatSymbols dateFormatSymbols = DateFormatSymbols.getInstance(locale);
        String[] labels = dateFormatSymbols.getMonths();
        return new MonthLabels(labels);
    }

    MonthLabels(String[] monthLabels) {
        this.monthLabels = monthLabels;
    }

    public String getMonthOfYear(@MonthInt int monthPosition) {
        return monthLabels[monthPosition - 1];
    }

    public String[] getMonthsOfYear() {
        return Arrays.copyOf(monthLabels, monthLabels.length);
    }
}
