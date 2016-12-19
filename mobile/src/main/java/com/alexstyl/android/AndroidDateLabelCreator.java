package com.alexstyl.android;

import android.content.Context;

import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.DateFormatUtils;
import com.alexstyl.specialdates.search.DateLabelCreator;

final public class AndroidDateLabelCreator implements DateLabelCreator {

    private static final boolean IGNORE_TODAY = false;
    private final Context context;

    public AndroidDateLabelCreator(Context context) {
        this.context = context;
    }

    @Override
    public String createLabelFor(Date date) {
        return DateFormatUtils.formatTimeStampString(context, date.toMillis(), IGNORE_TODAY);
    }
}
