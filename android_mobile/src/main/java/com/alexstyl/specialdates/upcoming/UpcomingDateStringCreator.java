package com.alexstyl.specialdates.upcoming;

import android.content.Context;
import android.text.format.DateUtils;

import com.alexstyl.resources.Strings;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.DateComparator;

public final class UpcomingDateStringCreator {

    private static final String DAY_OF_WEEK_SEPARATOR = ", ";

    private final Strings strings;
    private final Date today;
    private final Context context;

    UpcomingDateStringCreator(Strings strings, Date today, Context context) {
        this.strings = strings;
        this.today = today;
        this.context = context;
    }

    String createLabelFor(Date date) {
        int formatFlags = DateUtils.FORMAT_NO_NOON_MIDNIGHT | DateUtils.FORMAT_CAP_AMPM | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NO_YEAR;

        StringBuilder stringBuilder = new StringBuilder();

        if (isToday(date)) {
            stringBuilder.append(strings.today()).append(DAY_OF_WEEK_SEPARATOR);
        } else if (isTomorrow(date)) {
            stringBuilder.append(strings.tomorrow()).append(DAY_OF_WEEK_SEPARATOR);
        } else {
            formatFlags |= (DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_WEEKDAY);
        }

        if (date.getYear() != today.getYear()) {
            formatFlags |= DateUtils.FORMAT_SHOW_YEAR;
        }
        stringBuilder.append(DateUtils.formatDateTime(context, date.toMillis(), formatFlags));
        return stringBuilder.toString();
    }

    private boolean isToday(Date date) {
        return DateComparator.INSTANCE.compare(date, today) == 0;
    }

    private boolean isTomorrow(Date date) {
        return date.toMillis() - today.toMillis() == DateUtils.DAY_IN_MILLIS;
    }

}
