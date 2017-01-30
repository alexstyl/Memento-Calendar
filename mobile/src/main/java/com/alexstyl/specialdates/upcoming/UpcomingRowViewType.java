package com.alexstyl.specialdates.upcoming;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.alexstyl.specialdates.upcoming.UpcomingRowViewType.UPCOMING_EVENTS;
import static com.alexstyl.specialdates.upcoming.UpcomingRowViewType.MONTH_HEADER;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
        MONTH_HEADER,
        UPCOMING_EVENTS
})
public @interface UpcomingRowViewType {

    int MONTH_HEADER = 0;
    int UPCOMING_EVENTS = 1;
}
