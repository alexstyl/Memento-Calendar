package com.alexstyl.specialdates.search;

import android.content.Context;

import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.DateFormatUtils;

final class AndroidDateLabelCreator implements DateLabelCreator {

    private static final boolean IGNORE_TODAY = false;
    private final Context context;

    AndroidDateLabelCreator(Context context) {
        this.context = context;
    }

    @Override
    public String createLabelFor(Date date) {
        return DateFormatUtils.formatTimeStampString(context, date.toMillis(), IGNORE_TODAY);
    }
}
