package com.alexstyl.specialdates.date;

import android.content.Context;
import android.text.format.DateUtils;

import com.alexstyl.specialdates.MementoApplication;
import com.alexstyl.specialdates.contact.Birthday;

public final class DateDisplayStringCreator {

    private static final String SEPARATOR = "-";
    private static final int NO_YEAR = DayDate.NO_YEAR;
    private static final String ZERO = "0";

    private static DateDisplayStringCreator instance;

    public static DateDisplayStringCreator getInstance() {
        if (instance == null) {
            instance = new DateDisplayStringCreator();
        }
        return instance;
    }

    public String stringOf(Date date) {
        StringBuilder str = new StringBuilder();
        addYear(date, str);
        str.append(SEPARATOR);
        addMonth(date, str);
        str.append(SEPARATOR);
        addDayOfMonth(date, str);
        return str.toString();
    }

    private void addYear(Date date, StringBuilder str) {
        if (date.getYear() != NO_YEAR) {
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

    public String fullyFormattedBirthday(Birthday birthday) {
        DayDate dayDate = DayDate.newInstance(birthday.getDayOfMonth(), birthday.getMonth(), birthday.getYear());
        Context appContext = MementoApplication.getAppContext();

        int format_flags = DateUtils.FORMAT_NO_NOON_MIDNIGHT | DateUtils.FORMAT_CAP_AMPM | DateUtils.FORMAT_SHOW_DATE;

        if (birthday.includesYear()) {
            format_flags |= DateUtils.FORMAT_SHOW_YEAR;
        } else {
            format_flags |= DateUtils.FORMAT_NO_YEAR;
        }

        return DateUtils.formatDateTime(appContext, dayDate.toMillis(), format_flags);
    }

    public String fullyFormattedDate(Date date) {
        DayDate dayDate = DayDate.newInstance(date.getDayOfMonth(), date.getMonth(), date.getYear());
        Context appContext = MementoApplication.getAppContext();

        int format_flags = DateUtils.FORMAT_NO_NOON_MIDNIGHT | DateUtils.FORMAT_CAP_AMPM | DateUtils.FORMAT_SHOW_DATE;

        if (includesYear(date)) {
            format_flags |= DateUtils.FORMAT_SHOW_YEAR;
        } else {
            format_flags |= DateUtils.FORMAT_NO_YEAR;
        }

        return DateUtils.formatDateTime(appContext, dayDate.toMillis(), format_flags);

    }

    private boolean includesYear(Date date) {
        return date.getYear() != Date.NO_YEAR;
    }
}
