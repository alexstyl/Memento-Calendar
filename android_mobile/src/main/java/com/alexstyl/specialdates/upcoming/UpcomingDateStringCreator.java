package com.alexstyl.specialdates.upcoming;

import android.text.format.DateUtils;

import com.alexstyl.resources.StringResources;
import com.alexstyl.specialdates.MementoApplication;
import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.date.Date;
import com.alexstyl.specialdates.date.DateComparator;

final public class UpcomingDateStringCreator {

    private static final String DAY_OF_WEEK_SEPARATOR = ", ";

    private final StringResources stringResources;
    private final Date today;

    UpcomingDateStringCreator(StringResources stringResources, Date today) {
        this.stringResources = stringResources;
        this.today = today;
    }

    public String createLabelFor(Date date) {
        int format_flags = DateUtils.FORMAT_NO_NOON_MIDNIGHT | DateUtils.FORMAT_CAP_AMPM | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NO_YEAR;

        StringBuilder stringBuilder = new StringBuilder();

        if (isToday(date)) {
            stringBuilder.append(stringResources.getString(R.string.today)).append(DAY_OF_WEEK_SEPARATOR);
        } else if (isTomorrow(date)) {
            stringBuilder.append(stringResources.getString(R.string.tomorrow)).append(DAY_OF_WEEK_SEPARATOR);
        } else {
            format_flags |= (DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_WEEKDAY);
        }

        if (date.getYear() != today.getYear()) {
            format_flags |= DateUtils.FORMAT_SHOW_YEAR;
        }
        stringBuilder.append(DateUtils.formatDateTime(MementoApplication.getContext(), date.toMillis(), format_flags));
        return stringBuilder.toString();
    }

    private boolean isToday(Date date) {
        return DateComparator.INSTANCE.compare(date, today) == 0;
    }

    private boolean isTomorrow(Date date) {
        return date.toMillis() - today.toMillis() == DateUtils.DAY_IN_MILLIS;
    }

}
