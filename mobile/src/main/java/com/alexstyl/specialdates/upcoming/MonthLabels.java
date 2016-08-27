package com.alexstyl.specialdates.upcoming;

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

    public String getMonthOfYear(int monthPosition) {
        checkArgument(monthPosition);
        return monthLabels[monthPosition - 1];
    }

    private void checkArgument(int month) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("There are only 12 months in a year. [" + month + "] is not one of them");
        }
    }

    public String[] getMonthsOfYear() {
        return Arrays.copyOf(monthLabels, monthLabels.length);
    }
}
