package com.alexstyl.specialdates.events.peopleevents;

import com.alexstyl.specialdates.date.Date;

/**
 * Creates date labels in a YYYY-MM-DD fashion. This class should be used to describe a date for non-user facing components, such as database entries.
 */
public class ShortDateLabelCreator {

    private static final int TEN = 10;
    private static final String SEPARATOR = "-";
    private static final String ZERO = "0";

    public String createLabelWithYearPreferredFor(Date date) {
        StringBuilder str = new StringBuilder();
        addYear(date, str);
        str.append(SEPARATOR);
        addMonth(date, str);
        str.append(SEPARATOR);
        addDayOfMonth(date, str);
        return str.toString();
    }

    public String createLabelWithNoYearFor(Date date) {
        StringBuilder str = new StringBuilder();
        addMonth(date, str);
        str.append(SEPARATOR);
        addDayOfMonth(date, str);
        return str.toString();
    }

    private void addYear(Date date, StringBuilder str) {
        if (date.hasYear()) {
            str.append(date.getYear());
        } else {
            str.append(SEPARATOR);
        }
    }

    private void addMonth(Date date, StringBuilder str) {
        boolean isSingleDigit = isSingleDigit(date.getMonth());
        if (isSingleDigit) {
            str.append(ZERO);
        }
        str.append(date.getMonth());
    }

    private void addDayOfMonth(Date date, StringBuilder str) {
        boolean isSingleDigit = isSingleDigit(date.getDayOfMonth());
        if (isSingleDigit) {
            str.append(ZERO);
        }
        str.append(date.getDayOfMonth());
    }

    private boolean isSingleDigit(int number) {
        return number < TEN;
    }
}
