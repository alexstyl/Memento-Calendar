package com.alexstyl.specialdates.date;

import android.content.Context;
import android.text.format.DateUtils;

import com.alexstyl.specialdates.MementoApplication;
import com.alexstyl.specialdates.contact.Birthday;

public final class DateDisplayStringCreator {

    private static final String SEPARATOR = "-";
    private static final String ZERO = "0";

    private static DateDisplayStringCreator instance;

    public static DateDisplayStringCreator getInstance() {
        if (instance == null) {
            instance = new DateDisplayStringCreator();
        }
        return instance;
    }

    public String stringOf(DayDate date) {
        StringBuilder str = new StringBuilder();
        addYear(date, str);
        str.append(SEPARATOR);
        addMonth(date, str);
        str.append(SEPARATOR);
        addDayOfMonth(date, str);
        return str.toString();
    }

    public String stringOf(AnnualEvent date) {
        StringBuilder str = new StringBuilder();
        str.append(SEPARATOR);
        str.append(SEPARATOR);
        addMonth(date, str);
        str.append(SEPARATOR);
        addDayOfMonth(date, str);
        return str.toString();
    }

    private void addYear(DayDate date, StringBuilder str) {
        str.append(date.getYear());
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
        DayDate dayDate = getDayDateFor(birthday);
        Context appContext = MementoApplication.getContext();

        int format_flags = DateUtils.FORMAT_NO_NOON_MIDNIGHT | DateUtils.FORMAT_CAP_AMPM | DateUtils.FORMAT_SHOW_DATE;

        if (birthday.hasYearOfBirth()) {
            format_flags |= DateUtils.FORMAT_SHOW_YEAR;
        } else {
            format_flags |= DateUtils.FORMAT_NO_YEAR;
        }

        return DateUtils.formatDateTime(appContext, dayDate.toMillis(), format_flags);
    }

    public DayDate getDayDateFor(Birthday birthday) {
        int year;
        if (birthday.hasYearOfBirth()) {
            year = birthday.getYear();
        } else {
            year = DayDate.todaysYear();
        }
        return DayDate.newInstance(birthday.getDayOfMonth(), birthday.getMonth(), year);
    }

    public String fullyFormattedDate(DayDate dayDate) {
        Context appContext = MementoApplication.getContext();

        int format_flags = DateUtils.FORMAT_NO_NOON_MIDNIGHT | DateUtils.FORMAT_CAP_AMPM | DateUtils.FORMAT_SHOW_DATE;
        format_flags |= DateUtils.FORMAT_SHOW_YEAR;
        return DateUtils.formatDateTime(appContext, dayDate.toMillis(), format_flags);

    }
}
