package com.alexstyl.specialdates.upcoming;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.alexstyl.specialdates.upcoming.UpcomingRowViewType.UPCOMING_EVENTS;
import static com.alexstyl.specialdates.upcoming.UpcomingRowViewType.MONTH;
import static com.alexstyl.specialdates.upcoming.UpcomingRowViewType.YEAR;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
        MONTH,
        UPCOMING_EVENTS,
        YEAR
})
public @interface UpcomingRowViewType {

    int MONTH = 0;
    int UPCOMING_EVENTS = 1;
    int YEAR = 2;
}
