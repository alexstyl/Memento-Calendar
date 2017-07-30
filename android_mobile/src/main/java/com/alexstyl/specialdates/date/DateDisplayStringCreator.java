package com.alexstyl.specialdates.date;

import android.content.Context;
import android.text.format.DateUtils;

import com.alexstyl.specialdates.MementoApplication;

public enum DateDisplayStringCreator {
    INSTANCE;

    private static final String SEPARATOR = "-";
    private static final String ZERO = "0";

    public String stringOf(Date date) {
        StringBuilder str = new StringBuilder();
        addYear(date, str);
        str.append(SEPARATOR);
        addMonth(date, str);
        str.append(SEPARATOR);
        addDayOfMonth(date, str);
        return str.toString();
    }

    public String stringOfNoYear(Date date) {
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
        return number < 10;
    }

    public String fullyFormattedBirthday(Date birthday) {
        Context appContext = MementoApplication.getContext();

        int format_flags = DateUtils.FORMAT_NO_NOON_MIDNIGHT | DateUtils.FORMAT_CAP_AMPM | DateUtils.FORMAT_SHOW_DATE;

        if (birthday.hasYear()) {
            format_flags |= DateUtils.FORMAT_SHOW_YEAR;
        } else {
            format_flags |= DateUtils.FORMAT_NO_YEAR;
        }

        return DateUtils.formatDateTime(appContext, birthday.toMillis(), format_flags);
    }

    public String fullyFormattedDate(Date date) {
        Context appContext = MementoApplication.getContext();

        int format_flags = DateUtils.FORMAT_NO_NOON_MIDNIGHT | DateUtils.FORMAT_CAP_AMPM | DateUtils.FORMAT_SHOW_DATE;
        if (date.hasYear()) {
            format_flags |= DateUtils.FORMAT_SHOW_YEAR;
        } else {
            format_flags |= DateUtils.FORMAT_NO_YEAR;
        }
        return DateUtils.formatDateTime(appContext, date.toMillis(), format_flags);
    }
}
