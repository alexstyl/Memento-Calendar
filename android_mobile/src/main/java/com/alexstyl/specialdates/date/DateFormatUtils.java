package com.alexstyl.specialdates.date;

import android.content.Context;
import android.text.format.DateUtils;

import com.alexstyl.specialdates.R;

import java.util.Calendar;

/**
 * @deprecated Use {@link DateDisplayStringCreator} instead
 */
@Deprecated // TODO Get rid of this class in favor of DateDisplayStringCreator
public class DateFormatUtils {

    private DateFormatUtils() {
        // hide this
    }

    public static String formatTimeStampString(Context context, long when, boolean includeToday) {
        return formatTimeStampString(context, when, includeToday, false);
    }

    public static String formatTimeStampString(Context context, long when, boolean includeToday, boolean includeYear) {
        // Basic settings for formatDateTime() we want for all cases.
        @SuppressWarnings("deprecation")
        int formatFlags = DateUtils.FORMAT_NO_NOON_MIDNIGHT
                | DateUtils.FORMAT_CAP_AMPM | DateUtils.FORMAT_SHOW_DATE;

        if (!includeYear) {
            formatFlags |= DateUtils.FORMAT_NO_YEAR;
        }

        String day = "";
        if (includeToday) {
            if (isInXDays(when, 0)) {
                day = context.getString(R.string.today) + ", ";
            } else if (isInXDays(when, 1)) {
                day = context.getString(R.string.tomorrow) + ", ";
            } else {
                formatFlags |= (DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_WEEKDAY);
            }
        } else {
            formatFlags |= (DateUtils.FORMAT_SHOW_DATE);
        }

        return day + DateUtils.formatDateTime(context, when, formatFlags);

    }

    /**
     * @return true if the supplied when is today
     */
    private static boolean isInXDays(long when, int value) {
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(when);

        int thenYear = time.get(Calendar.YEAR);
        int thenMonth = time.get(Calendar.MONTH);
        int thenMonthDay = time.get(Calendar.DAY_OF_MONTH);

        time = Calendar.getInstance();
        time.add(Calendar.DAY_OF_MONTH, value);
        return thenMonthDay == time.get(Calendar.DAY_OF_MONTH)
                && thenYear == time.get(Calendar.YEAR)
                && thenMonth == time.get(Calendar.MONTH);
    }
}
