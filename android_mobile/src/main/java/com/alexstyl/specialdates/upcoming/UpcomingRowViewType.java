package com.alexstyl.specialdates.upcoming;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.alexstyl.specialdates.upcoming.UpcomingRowViewType.*;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
        MONTH,
        UPCOMING_EVENTS,
        YEAR,
        AD,
        DATE_HEADER,
        CONTACT_EVENT,
        NAMEDAY_CARD,
        BANKHOLIDAY
})
public @interface UpcomingRowViewType {
    int MONTH = 0;
    int UPCOMING_EVENTS = 1;
    int YEAR = 2;
    int AD = 3;
    int DATE_HEADER = 4;
    int CONTACT_EVENT = 5;
    int NAMEDAY_CARD = 6;
    int BANKHOLIDAY = 7;
}
