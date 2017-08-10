package com.alexstyl.specialdates;

import com.alexstyl.specialdates.date.Date;

public final class SQLArgumentBuilder {

    private static final int TEN = 10;

    private SQLArgumentBuilder() {
        // hide this
    }

    public static String dateWithoutYear(Date date) {
        StringBuilder stringBuilder = new StringBuilder();
        int month = date.getMonth();
        addWithLeadingZeroIfNeeded(stringBuilder, month);
        stringBuilder.append("-");
        addWithLeadingZeroIfNeeded(stringBuilder, date.getDayOfMonth());
        return stringBuilder.toString();
    }

    private static void addWithLeadingZeroIfNeeded(StringBuilder stringBuilder, int value) {
        if (value < TEN) {
            stringBuilder.append("0");
        }
        stringBuilder.append(value);
    }
}
