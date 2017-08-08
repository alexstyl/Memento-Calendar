package com.alexstyl.specialdates.date;

import android.content.Context;
import android.text.format.DateUtils;

import com.alexstyl.specialdates.search.DateLabelCreator;

public final class AndroidDateLabelCreator implements DateLabelCreator {

    private final Context context;

    public AndroidDateLabelCreator(Context context) {
        this.context = context;
    }

    @Override
    public String createLabelWithoutYearFor(Date date) {
        int formatFlags = DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NO_YEAR;
        return DateUtils.formatDateTime(context, date.toMillis(), formatFlags);
    }

    @Override
    public String createLabelWithYearFor(Date date) {
        int formatFlags = DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR;
        return DateUtils.formatDateTime(context, date.toMillis(), formatFlags);
    }
}
